package com.test.httpforward.fwdserver.nettyweb;

import com.test.httpforward.fwdserver.nettyweb.handler.HttpRequestInHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class NettyHttpServer {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private HttpRequestInHandler httpRequestInHandler;
    @Value("${http.netty.web.port:9090}")
    private int webPort;

    @PostConstruct
    public void serverStart(){
        logger.info(">>>>>>>>>NettyHttpServer init start<<<<<<<<<<<");
        new Thread(()->{
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
                pipeline.addLast("http-decoder", new HttpRequestDecoder());
                // 将HTTP消息的多个部分合成一条完整的HTTP消息
                pipeline.addLast("http-aggregator", new HttpObjectAggregator(65535));
                // 响应转码器
                pipeline.addLast("http-encoder", new HttpResponseEncoder());
                // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
                pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                // 自定义处理handler
                pipeline.addLast(httpRequestInHandler);
            }
        });

//        //6.设置参数
//        //设置参数，TCP参数
//        serverBootstrap.option(ChannelOption.SO_BACKLOG, 2048);         //连接缓冲池的大小
//        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//维持链接的活跃，清除死链接
//        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);//关闭延迟发送

        //7.绑定ip和port
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(webPort).sync();//Future模式的channel对象
            logger.info(">>>>>>>>>NettyHttpServer init success...webPort:{}<<<<<<<<<<<", webPort);
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
