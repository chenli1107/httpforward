package com.test.httpforward.fwdclient.netty.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.fwdcommon.entity.req.HttpPackageReq;
import com.test.fwdcommon.entity.rsp.HttpPackageRsp;
import com.test.fwdcommon.utils.HttpUtils;
import com.test.fwdcommon.face.BaseInServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HttpPackageReqService implements BaseInServer<HttpPackageReq> {
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean supports(Object msg) {
        return msg instanceof HttpPackageReq;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpPackageReq msg) throws Exception {
        log.info("receive req: {}", new ObjectMapper().writeValueAsString(msg));
        HttpPackageRsp rsp = httpRequestSend(msg);
        if (rsp == null)
            return;
        ctx.channel().writeAndFlush(mapper.writeValueAsString(rsp)).addListener((GenericFutureListener) future -> {
            log.info("send request[{}] rsp:{}", msg.getRequestId(), mapper.writeValueAsString(rsp));
        });
    }

    /**
     * http调用。。。待优调整
     * @param req
     * @return
     */
    // TODO http调用。。。待优调整
    private HttpPackageRsp httpRequestSend(HttpPackageReq req) {
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

        HttpPackageRsp response = new HttpPackageRsp();
        response.setRequestId(req.getRequestId());
        response.setHttpVersion(HttpVersion.HTTP_1_1.text());

        ResponseEntity<String> rsp = null;
        try {
            //rsp =  HttpUtils.getText("https://www.baidu.com/s?ie=UTF-8&wd=163", null, null); //有问题
            //rsp =  HttpUtils.getText("https://www.baidu.com", null, null); //成功
            //rsp =  HttpUtils.getText("http://localhost:8080/ccc/ssss", null, null);
            if (HttpMethod.POST.equals(method)) {
                rsp = HttpUtils.postJson(req.getUrl(), content, headerMap);
            } else if (HttpMethod.GET.equals(method)) {
                rsp = HttpUtils.getText(req.getUrl(), null, headerMap);
            }
        }catch (HttpClientErrorException he){
            response.setHttpRspStatus(new HttpPackageRsp.HttpRspStatus(he.getStatusCode().value(), he.getStatusCode().getReasonPhrase()));
            response.setContentBytes(he.getResponseBodyAsString().getBytes(Charset.defaultCharset()));
            log.warn("http请求失败.url[{}]", req.getUrl(), he);
            return response;
        }catch (Exception e){
            HttpPackageRsp err = errorResponse(req.getRequestId(), req.getUrl(), e.getMessage());
            log.error("http请求失败.url[{}]", req.getUrl(), e);
            return err;
        }


        response.setHttpRspStatus(new HttpPackageRsp.HttpRspStatus(rsp.getStatusCode().value(), rsp.getStatusCode().getReasonPhrase()));
        response.setContentBytes(rsp.getBody().getBytes(Charset.defaultCharset()));
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
        return response;
    }


    private HttpPackageRsp errorResponse(String requestId, String requestUrl, String errorContent ){
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
        HttpPackageRsp response = new HttpPackageRsp();
        response.setRequestId(requestId);
        response.setHttpVersion(HttpVersion.HTTP_1_1.text());
        response.setHttpRspStatus(
                new HttpPackageRsp.HttpRspStatus(HttpResponseStatus.FORBIDDEN.code(), HttpResponseStatus.FORBIDDEN.reasonPhrase()));
        response.setHeaders(headers);
        response.setContentBytes(errorContent.getBytes(Charset.defaultCharset()));
        return response;
    }


}
