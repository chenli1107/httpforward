package com.test.httpforward.fwdclient.netty.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.test.fwdcommon.entity.HttpRequestEntity;
import com.test.fwdcommon.entity.HttpResponseEntity;
import com.test.fwdcommon.utils.HttpResponseStatusSerializer;
import com.test.fwdcommon.utils.HttpUtils;
import com.test.httpforward.fwdclient.netty.server.BaseInServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HttpRequestIn implements BaseInServer<HttpRequestEntity> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    static final ObjectMapper mapper222 = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addSerializer(HttpResponseStatus.class, new HttpResponseStatusSerializer());
        mapper222.registerModule(module);
    }

    @Override
    public boolean supports(Object msg) {
        return msg instanceof HttpRequestEntity;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpRequestEntity msg) throws Exception {
        logger.info("receive req: {}", new ObjectMapper().writeValueAsString(msg));
        HttpResponseEntity rsp = httpRequestSend(msg);
        if (rsp == null)
            return;
        String rspStr = mapper222.writeValueAsString(rsp);
        ctx.channel().writeAndFlush(rspStr).addListener((GenericFutureListener) future -> {
            logger.info("send request[{}] rsp:{}", msg.getRequestId(), rspStr);
        });
    }

    /**
     * http调用。。。待优调整
     * @param req
     * @return
     */
    // TODO http调用。。。待优调整
    private HttpResponseEntity httpRequestSend(HttpRequestEntity req) {
        HttpMethod method = HttpMethod.valueOf(req.getMethodName());
        String content = req.getContentBytes()==null ? null : new String(req.getContentBytes(), Charset.defaultCharset());
        Map<String, String> headerMap = new HashMap<>();
        if(req.getHeaders() != null) {
            req.getHeaders().forEach((ent)->{
                if(headerMap.containsKey(ent.getKey())){
                    headerMap.put(ent.getKey(), headerMap.get(ent.getKey())+";"+ent.getValue());
                }else
                    headerMap.put(ent.getKey(), ent.getValue());
            });
        }

        ResponseEntity<String> rsp = null;
        try {
            //rsp =  HttpUtils.getText("http://localhost:8080/ccc/ssss", null, null);
            if (HttpMethod.POST.equals(method)) {
                rsp = HttpUtils.postJson(req.getUrl(), content, headerMap);
            } else if (HttpMethod.GET.equals(method)) {
                rsp = HttpUtils.getText(req.getUrl(), null, headerMap);
            }
        }catch (Exception e){
            HttpResponseEntity err = errorResponse(req.getRequestId(), req.getUrl(), e.getMessage());
            logger.warn("http请求失败.url[{}],exception[{}]", req.getUrl(), e.getMessage());
            return err;
        }

        HttpResponseEntity response = new HttpResponseEntity();
        response.setRequestId(req.getRequestId());
        response.setHttpVersion(HttpVersion.HTTP_1_1.text());
        response.setHttpRspStatus(new HttpResponseStatus(rsp.getStatusCode().value(), rsp.getStatusCode().getReasonPhrase()));
        if(rsp.getHeaders()!=null){
            List<Map.Entry<String, String>> headers = new ArrayList<>();
            rsp.getHeaders().forEach((k, vList)->{
                vList.forEach((v)->{
                    headers.add(new Map.Entry<String, String>() {
                        @Override
                        public String getKey() {
                            return k;
                        }

                        @Override
                        public String getValue() {
                            return v;
                        }

                        @Override
                        public String setValue(String value) {
                            return v;
                        }
                    });
                });

            });
            response.setHeaders(headers);
        }

        response.setContentBytes(rsp.getBody().getBytes(Charset.defaultCharset()));
        return response;
    }


    private HttpResponseEntity errorResponse(String requestId, String requestUrl, String errorContent ){
        List<Map.Entry<String, String>> headers = new ArrayList<>();
        headers.add(new Map.Entry<String, String>() {
            @Override
            public String getKey() {
                return "Content-Type";
            }

            @Override
            public String getValue() {
                return "text/html;charset=UTF-8";
            }

            @Override
            public String setValue(String value) {
                return "text/html;charset=UTF-8";
            }
        });
        errorContent = "调用["+ requestUrl +"]失败."+ (errorContent==null? "" : errorContent);
        HttpResponseEntity response = new HttpResponseEntity();
        response.setRequestId(requestId);
        response.setHttpVersion(HttpVersion.HTTP_1_1.text());
        response.setHttpRspStatus(HttpResponseStatus.FORBIDDEN);
        response.setHeaders(headers);
        response.setContentBytes(errorContent.getBytes(Charset.defaultCharset()));
        return response;
    }


}
