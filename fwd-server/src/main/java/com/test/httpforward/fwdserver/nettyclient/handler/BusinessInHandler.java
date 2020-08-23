package com.test.httpforward.fwdserver.nettyclient.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.fwdcommon.entity.req.IdlePingEntity;
import com.test.httpforward.fwdserver.channel.service.IChannelRegService;
import com.test.fwdcommon.face.BaseInServer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@ChannelHandler.Sharable
@Slf4j
public class BusinessInHandler extends SimpleChannelInboundHandler<Object> {
    static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private List<BaseInServer> inServerList;
    @Autowired
    IChannelRegService channelRegService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null)
                return;
            for (int i = 0; i < inServerList.size(); i++) {
                BaseInServer sv = inServerList.get(i);
                if (sv.supports(msg)) {
                    sv.channelRead0(ctx, msg);
                    return;
                }
            }
        }catch (Exception e){
            log.error("sssssssss", e);
        }
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
        channelRegService.destroy(ctx.channel());
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
        channelRegService.destroy(ctx.channel());
        log.error("exceptionCaught..msg:{}", cause.getMessage());
        ctx.channel().close();
    }

    /**
     * 心跳
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                log.info("READER_IDLE");
                // 超时关闭channel
                ctx.close();
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                log.info("WRITER_IDLE");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                // 发送心跳
                IdlePingEntity ping = new IdlePingEntity();
                ping.setPingId(UUID.randomUUID().toString());
                ping.setPingTime(new Date());
                ctx.channel().writeAndFlush(mapper.writeValueAsString(ping));
                log.info("send ping channel:{}, id:{}", ctx.channel(), ping.getPingId());

            }
        }
        super.userEventTriggered(ctx, evt);
    }

}
