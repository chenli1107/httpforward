package com.test.httpforward.fwdclient.netty.handler;

import cn.hutool.json.JSONUtil;
import com.test.httpforward.fwdclient.config.AppConfig;
import com.test.httpforward.fwdclient.netty.ConnectionServer;
import com.test.fwdcommon.face.BaseInServer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ChannelHandler.Sharable
public class BusinessInHandler extends SimpleChannelInboundHandler<Object> {
    @Autowired
    AppConfig appConfig;
    @Autowired
    private List<BaseInServer> inServerList;
    @Autowired
    private ConnectionServer connectionServer;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg == null)
            return;
        for(BaseInServer sv : inServerList){
            if(sv.supports(msg)) {
                sv.channelRead0(ctx, msg);
                return;
            }
        }
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.channel().writeAndFlush(JSONUtil.toJsonStr(appConfig.getClientRegReq()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("与服务器断开连接服务器.将尝试重连"+ctx.channel());
        //重新连接服务器
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                connectionServer.connection();
            }
        }, 5, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

}
