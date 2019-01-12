package com.test.httpforward.fwdclient.netty.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;

public interface BaseInServer<T> {
    ObjectMapper mapper = new ObjectMapper();

    boolean supports(Object msg);

    void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception;
}
