package com.test.httpforward.fwdclient.netty.handler;

import com.test.fwdcommon.entity.HttpUrlMapping;
import com.test.httpforward.fwdclient.netty.ConnectionServer;
import com.test.httpforward.fwdclient.netty.server.BaseInServer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@ChannelHandler.Sharable
public class BusinessInHandler extends SimpleChannelInboundHandler<Object> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private List<BaseInServer> inServerList;
    @Autowired
    private ConnectionServer connectionServer;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg == null)
            return;
        msgHandle(ctx, msg);
    }

    private void msgHandle(ChannelHandlerContext ctx, Object msg) throws Exception {
        for(int i=0; i<inServerList.size(); i++){
            BaseInServer sv = inServerList.get(i);
            if(sv.supports(msg)) {
                sv.channelRead0(ctx, msg);
                return;
            }
        }
        return;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.error("与服务器断开连接服务器.将尝试重连"+ctx.channel());
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
