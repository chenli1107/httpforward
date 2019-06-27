package com.test.httpforward.fwdserver.nettyclient.server;

import com.test.fwdcommon.entity.HttpResponseEntity;
import com.test.fwdcommon.entity.HttpUrlMapping;
import com.test.httpforward.fwdserver.server.HttpRequestCacheManager;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.netty.handler.codec.rtsp.RtspHeaderNames.CONTENT_LENGTH;

@Service
public class HttpResponseEntityIn implements BaseInServer<HttpResponseEntity> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private HttpRequestCacheManager httpRequestCacheManager;

    @Override
    public boolean supports(Object msg) {
        return msg instanceof HttpResponseEntity;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpResponseEntity msg) throws Exception {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.valueOf(msg.getHttpVersion()), msg.getHttpRspStatus(),
                Unpooled.wrappedBuffer(msg.getContentBytes()));
        Optional.ofNullable(msg.getHeaders()).ifPresent(headers-> headers.forEach((he) -> {
            response.headers().add(he.getKey(), he.getValue());
        }));
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
        //向浏览器输出response
        Channel httpChannel = httpRequestCacheManager.remove(msg.getRequestId());
        if (httpChannel == null) {
            logger.warn("可能超时返回,无效的response..{}", response);
            return;
        }
        httpChannel.writeAndFlush(response);
        logger.info("http response: {}", response);

    }
}
