package com.test.httpforward.fwdserver.nettyweb.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.fwdcommon.entity.HttpRequestEntity;
import com.test.fwdcommon.entity.HttpUrlMapping;
import com.test.httpforward.fwdserver.server.HttpRequestCacheManager;
import com.test.httpforward.fwdserver.server.HttpURLMappingManager;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static io.netty.handler.codec.rtsp.RtspHeaderNames.CONTENT_LENGTH;

@Service
@ChannelHandler.Sharable
public class HttpRequestInHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private HttpRequestCacheManager httpRequestCacheManager;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String proxyServerPath = "/"+ (fullHttpRequest.uri().split("/", -1)[1]);
        HttpUrlMapping httpUrlMapping = urlMapping(channelHandlerContext, fullHttpRequest.uri(), proxyServerPath);
        if(httpUrlMapping==null)
            return;

        logger.debug(fullHttpRequest.uri());
        logger.debug(fullHttpRequest.headers().toString());
        logger.debug(fullHttpRequest.content().toString());

        HttpRequestEntity req = new HttpRequestEntity();
        //不支持https..因使用的web代理,浏览器域名、证书匹配不上
        req.setUrl("http://" +httpUrlMapping.getRealWebServerHost()
                + httpUrlMapping.getRealWebServerPath()
                + fullHttpRequest.uri().replaceFirst(httpUrlMapping.getProxyServerPath(), ""));
        req.setMethodName(fullHttpRequest.method().name());
        req.setHeaders(fullHttpRequest.headers().entries());
        byte[] bytes = new byte[fullHttpRequest.content().readableBytes()];
        fullHttpRequest.content().readBytes(bytes);
        req.setContentBytes(bytes);
        req.setRequestId(UUID.randomUUID().toString());
        req.setRequestTime(new Date());
        ObjectMapper mapper = new ObjectMapper();
        String msgStr = mapper.writeValueAsString(req);
        logger.info("req send: {}", msgStr);

        //请求转发至客户端
        httpUrlMapping.getProxyClientChannel().writeAndFlush(msgStr).addListeners(new GenericFutureListener(){
            @Override
            public void operationComplete(Future future) throws Exception {
                httpRequestCacheManager.put(req.getRequestId(), channelHandlerContext.channel());
            }
        });

    }

    /**
     * uri过滤+获取映射
     * @param channelHandlerContext
     * @param uri
     * @param proxyServerPath
     * @return
     */
    private HttpUrlMapping urlMapping(ChannelHandlerContext channelHandlerContext, String uri, String proxyServerPath){
        if(uri.endsWith("/favicon.ico"))
            return null;

        HttpUrlMapping httpUrlMapping = HttpURLMappingManager.instance().getHttpUrlMapping(proxyServerPath);
        if(httpUrlMapping == null) {
            logger.info("无注册该服务客户端[{}]", proxyServerPath);
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(("无注册该服务客户端"+proxyServerPath).getBytes()));
            response.headers().set("Content-Type", "text/plain; charset=UTF-8");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
            channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            return null;
        }
        return httpUrlMapping;
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
