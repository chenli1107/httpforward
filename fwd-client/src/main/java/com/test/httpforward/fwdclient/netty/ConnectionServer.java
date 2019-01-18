package com.test.httpforward.fwdclient.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.fwdcommon.entity.HttpUrlMapping;
import com.test.httpforward.fwdclient.netty.handler.BusinessInHandler;
import com.test.httpforward.fwdclient.netty.handler.JsonToObjectInHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@Component
public class ConnectionServer {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private BusinessInHandler businessInHandler;
    @Value("${forward.netty.server.ip:127.0.0.1}")
    private String serverIp;
    @Value("${forward.netty.server.port:9000}")
    private int serverPort;
    @Autowired
    private HttpUrlMapping httpUrlMapping;

    private Bootstrap bootstap;
    //2.定义执行线程组
    private EventLoopGroup worker = new NioEventLoopGroup();


    public ConnectionServer(){
        //1.定义服务类
        bootstap = new Bootstrap();
        //2.定义执行线程组
//        EventLoopGroup worker = new NioEventLoopGroup();
        //3.设置线程池
        bootstap.group(worker);
        //4.设置通道
        bootstap.channel(NioSocketChannel.class);
        //5.添加Handler
        bootstap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast(new JsonObjectDecoder());
                pipeline.addLast(new JsonToObjectInHandler());
                pipeline.addLast(businessInHandler);

                pipeline.addLast(new StringEncoder(Charset.defaultCharset()));
            }
        });
    }


    @PreDestroy
    public void destory(){
        worker.shutdownGracefully();
    }


    public void connection() {
        logger.info("开始建立连接...");
        try {
            //6.建立连接
            ChannelFuture channelFuture = bootstap.connect(serverIp, serverPort);
            channelFuture.addListener((ChannelFutureListener) cf -> {
                //启动时，链接失败重连
                if (!cf.isSuccess()) {
                    cf.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            logger.error("服务端链接不上，开始重连操作...");
                            connection();
                        }
                    }, 3L, TimeUnit.SECONDS);
                } else {
                    logger.info("服务端链接成功...");
                }
            });
            channelFuture.sync();
            //注册http映射
            sendRegist(channelFuture.channel(), httpUrlMapping);
            openSystemIn(channelFuture.channel());
//            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //8.关闭连接    //重连还是前实例channelFuture,此时不能关闭相关资源
//            worker.shutdownGracefully();
        }
    }


    /**
     * 控制台输入注册http映射
     * @param channel
     */
    public void openSystemIn(Channel channel){
        new Thread(()-> {
            try {
                //7.控制台输入
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                ObjectMapper mapper = new ObjectMapper();
                while (true) {
                    System.out.println("注册请输入...格式如：{\"realWebServerHost\": \"localhost:port\", \"realWebServerPath\": \"/server\",  \"proxyServerPath\": \"/ppp\"}");
                    String msg = bufferedReader.readLine();
                    try {
                        HttpUrlMapping mp = mapper.readValue(msg, HttpUrlMapping.class);
                        sendRegist(channel, mp);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     *
     * @param channel
     * @param mp
     * @return
     */
    public boolean sendRegist(Channel channel, HttpUrlMapping mp) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (mp == null || StringUtils.isEmpty(mp.getProxyServerPath())
                    || StringUtils.isEmpty(mp.getRealWebServerHost())) {
                logger.error("注册信息不完整.注册失败");
                return false;
            }
            String sp = mp.getRealWebServerPath();
            if (sp == null || "/".equals(sp))
                mp.setRealWebServerPath("");
            else if (sp.endsWith("/"))
                mp.setRealWebServerPath(sp.substring(0, sp.lastIndexOf("/")));

            channel.writeAndFlush(mapper.writeValueAsString(mp)).addListener((future) -> {
                logger.info("已发送注册信息,等待返回结果通知...");
            });
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }

    }


}
