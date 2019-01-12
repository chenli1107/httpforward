package com.test.httpforward.fwdserver.nettyclient.server;

import com.test.fwdcommon.entity.HttpUrlMapping;
import com.test.fwdcommon.entity.HttpUrlMappingResult;
import com.test.httpforward.fwdserver.server.HttpURLMappingManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HttpUrlMappingIn implements BaseInServer<HttpUrlMapping> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supports(Object msg) {
        return msg instanceof HttpUrlMapping;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpUrlMapping msg) throws Exception {
        msg.setProxyClientChannel(ctx.channel());
        HttpUrlMappingResult res = HttpURLMappingManager.instance().regist(msg);
        ctx.channel().writeAndFlush(mapper.writeValueAsString(res));
    }
}
