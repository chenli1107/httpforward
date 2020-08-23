package com.test.fwdcommon.entity.rsp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.fwdcommon.entity.BaseEntity;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
public class HttpPackageRsp extends BaseEntity {
    private String httpVersion;
    private HttpRspStatus httpRspStatus;
    private List<Map.Entry<String, String>> headers;
    private byte[] contentBytes;

    private String requestId;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HttpRspStatus{
        private int value;

        private String reasonPhrase;

        public HttpResponseStatus toHttpResponseStatus(){
            return new HttpResponseStatus(value, reasonPhrase);
        }
    }
}


