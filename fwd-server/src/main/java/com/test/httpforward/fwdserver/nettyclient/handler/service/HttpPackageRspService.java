package com.test.httpforward.fwdserver.nettyclient.handler.service;

import com.test.fwdcommon.entity.rsp.HttpPackageRsp;
import com.test.fwdcommon.face.BaseInServer;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class HttpPackageRspService implements BaseInServer<HttpPackageRsp> {
    @Autowired
    private HttpRequestCacheManager httpRequestCacheManager;

    @Override
    public boolean supports(Object msg) {
        return msg instanceof HttpPackageRsp;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpPackageRsp msg) throws Exception {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.valueOf(msg.getHttpVersion()), msg.getHttpRspStatus().toHttpResponseStatus(),
                Unpooled.wrappedBuffer(msg.getContentBytes()));
        Optional.ofNullable(msg.getHeaders()).ifPresent(headers-> headers.forEach((he) -> {
            response.headers().add(he.getKey(), he.getValue());
        }));
        //向浏览器输出response
        Channel httpChannel = httpRequestCacheManager.remove(msg.getRequestId());
        if (httpChannel == null) {
            log.warn("可能超时返回,无效的response..{}", response);
            return;
        }
        httpChannel.writeAndFlush(response);
        log.info("http response: {}", response);

    }
}
