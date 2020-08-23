package com.test.fwdcommon.face;

import io.netty.channel.ChannelHandlerContext;

/**
 * fwd的netty消息接收处理接口（client、server都使用此接口）
 * @param <T>
 */
public interface BaseInServer<T> {

    boolean supports(Object msg);

    void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception;
}
