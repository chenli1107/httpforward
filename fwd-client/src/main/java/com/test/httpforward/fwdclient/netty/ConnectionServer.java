package com.test.httpforward.fwdclient.netty;

import com.test.httpforward.fwdclient.config.AppConfig;
import com.test.httpforward.fwdclient.netty.handler.BusinessInHandler;
import com.test.httpforward.fwdclient.netty.handler.JsonToObjectInHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ConnectionServer {
    @Autowired
    private BusinessInHandler businessInHandler;
    @Autowired
    private AppConfig appConfig;

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
        log.info("开始建立连接...");
        try {
            //6.建立连接
            ChannelFuture channelFuture = bootstap.connect(appConfig.getServerIp(), appConfig.getServerPort());
            channelFuture.addListener((ChannelFutureListener) cf -> {
                //启动时，链接失败重连
                if (!cf.isSuccess()) {
                    cf.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            log.error("服务端链接不上，开始重连操作...");
                            connection();
                        }
                    }, 3L, TimeUnit.SECONDS);
                } else {
                    log.info("服务端链接成功...");
                }
            });
            channelFuture.sync();

//            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("connection exception!", e);
        } finally {
            //8.关闭连接    //重连还是前实例channelFuture,此时不能关闭相关资源
//            worker.shutdownGracefully();
        }
    }
}
