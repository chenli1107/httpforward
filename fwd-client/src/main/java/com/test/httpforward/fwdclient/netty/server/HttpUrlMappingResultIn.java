package com.test.httpforward.fwdclient.netty.server;

import com.test.fwdcommon.entity.HttpUrlMappingResult;
import com.test.httpforward.fwdclient.netty.server.BaseInServer;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HttpUrlMappingResultIn implements BaseInServer<HttpUrlMappingResult> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supports(Object msg) {
        return msg instanceof HttpUrlMappingResult;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpUrlMappingResult msg) throws Exception {
        logger.info("注册结果:{}", mapper.writeValueAsString(msg));
    }
}
