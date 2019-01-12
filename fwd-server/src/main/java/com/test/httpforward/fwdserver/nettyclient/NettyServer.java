package com.test.httpforward.fwdserver.nettyclient;

import com.test.httpforward.fwdserver.nettyclient.handler.BusinessInHandler;
import com.test.httpforward.fwdserver.nettyclient.handler.JsonToObjectInHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@Component
public class NettyServer {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private BusinessInHandler businessInHandler;
    @Value("${forward.netty.server.port:9000}")
    private int serverPort;

    @PostConstruct
    public void serverStart() {
        logger.info(">>>>>>>>>NettyServer init start<<<<<<<<<<<");
        new Thread(() -> {
            start();
        }).start();
    }

    public void start(){
        //1.定义server启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //2.定义工作组:boss分发请求给各个worker:boss负责监听端口请求，worker负责处理请求（读写）
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        //3.定义工作组
        serverBootstrap.group(boss,worker);
        //4.设置通道channel
        serverBootstrap.channel(NioServerSocketChannel.class);
        //5.添加handler，管道中的处理器，通过ChannelInitializer来构造
        serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                //此方法每次客户端连接都会调用，是为通道初始化的方法
                //获得通道channel中的管道链（执行链、handler链）
                ChannelPipeline pipeline = channel.pipeline();

                // 请求解码器
                pipeline.addLast("ping", new IdleStateHandler(60, 0, 15,
                        TimeUnit.SECONDS));
                pipeline.addLast(new JsonObjectDecoder());
                pipeline.addLast(new JsonToObjectInHandler());
                pipeline.addLast(businessInHandler);
                pipeline.addLast(new StringEncoder(Charset.defaultCharset()));

            }
        });

        //7.绑定ip和port
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(serverPort).sync();//Future模式的channel对象
            logger.info(">>>>>>>>>NettyServer init success. serverPort:{}<<<<<<<<<<<", serverPort);
            //7.5.监听关闭
            channelFuture.channel().closeFuture().sync();  //等待服务关闭，关闭后应该释放资源
        } catch (InterruptedException e) {
            System.out.println("server start got exception!");
            e.printStackTrace();
        }finally {
            //8.优雅的关闭资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
