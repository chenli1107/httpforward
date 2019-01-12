package com.test.httpforward.fwdserver.nettyclient.handler;

import com.test.httpforward.fwdserver.nettyclient.server.BaseInServer;
import com.test.httpforward.fwdserver.server.HttpURLMappingManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ChannelHandler.Sharable
public class BusinessInHandler extends SimpleChannelInboundHandler<Object> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private List<BaseInServer> inServerList;

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

    /*
 * 覆盖channelActive 方法在channel被启用的时候触发（在建立连接的时候）
 * 覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动
 */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    /*
     * (non-Javadoc)
     * 覆盖了 handlerAdded() 事件处理方法。
     * 每当从服务端收到新的客户端连接时
     */
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    /*
     * (non-Javadoc)
     * .覆盖了 handlerRemoved() 事件处理方法。
     * 每当从服务端收到客户端断开时
     */
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        HttpURLMappingManager.instance().cancel(ctx.channel());
        super.handlerRemoved(ctx);
    }

    /*
     * exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用，
     * 即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时。
     * 在大部分情况下，捕获的异常应该被记录下来并且把关联的 channel 给关闭掉。
     * 然而这个方法的处理方式会在遇到不同异常的情况下有不同的实现，
     * 比如你可能想在关闭连接之前发送一个错误码的响应消息。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        HttpURLMappingManager.instance().cancel(ctx.channel());
        logger.error(cause.getMessage());
        ctx.channel().close();
    }


}
