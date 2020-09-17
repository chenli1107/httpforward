package com.test.httpforward.fwdserver.nettyweb.handler;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.fwdcommon.entity.req.HttpPackageReq;
import com.test.httpforward.fwdserver.channel.service.IChannelRegService;
import com.test.httpforward.fwdserver.nettyclient.handler.service.HttpRequestCacheManager;
import com.test.httpforward.fwdserver.urlReg.bean.UrlRegVo;
import com.test.httpforward.fwdserver.urlReg.service.IUrlRegService;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@ChannelHandler.Sharable
public class HttpPackageReqHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Autowired
    private HttpRequestCacheManager httpRequestCacheManager;
    @Autowired
    IUrlRegService urlRegService;
    @Autowired
    IChannelRegService channelRegService;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if(fullHttpRequest.uri().endsWith("/favicon.ico"))
            return;

        String proxyServerPath = "/"+ (fullHttpRequest.uri().split("/", -1)[1]);
        UrlRegVo urlRegVo = urlRegService.getByProxyServerPath(proxyServerPath);
        if(urlRegVo==null) {
            // 未注册的url
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
            channelHandlerContext.channel().writeAndFlush(response);
            return;
        }
        Channel clientChannel = channelRegService.getByCertificates(urlRegVo.getCertificates());
        if(clientChannel == null || !clientChannel.isOpen()){
            // 通道未建立
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.GATEWAY_TIMEOUT);
            channelHandlerContext.channel().writeAndFlush(response);
            return;
        }

        log.debug(fullHttpRequest.uri());
        log.debug(fullHttpRequest.headers().toString());
        log.debug(fullHttpRequest.content().toString());

        byte[] bytes = new byte[fullHttpRequest.content().readableBytes()];
        fullHttpRequest.content().readBytes(bytes);
        HttpPackageReq httpPackageReq = HttpPackageReq.builder()
                .url(urlRegVo.getDto().getClientWebServerPath()
                        + fullHttpRequest.uri().replaceFirst(urlRegVo.getDto().getProxyServerPath(), ""))//不支持https..因使用的web代理,浏览器域名、证书匹配不上
                .methodName(fullHttpRequest.method().name())
                .headers(fullHttpRequest.headers().entries())
                .contentBytes(bytes)
                .requestId(UUID.randomUUID().toString())
                .requestTime(new Date())
                .build();
        //请求转发至客户端
        ObjectMapper mapper = new ObjectMapper();
        String msgStr = mapper.writeValueAsString(httpPackageReq);
        clientChannel.writeAndFlush(msgStr).addListeners(new GenericFutureListener(){
            @Override
            public void operationComplete(Future future) throws Exception {
                httpRequestCacheManager.put(httpPackageReq.getRequestId(), channelHandlerContext.channel());
            }
        });

    }

    /**
     * http handler断开连接导致异常是很平常的事
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.channel().close();
    }
}
