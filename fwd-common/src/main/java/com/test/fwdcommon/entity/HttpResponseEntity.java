package com.test.fwdcommon.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.util.List;
import java.util.Map;

public class HttpResponseEntity extends BaseEntity{
    private String httpVersion;
    private HttpResponseStatus httpRspStatus;
    private List<Map.Entry<String, String>> headers;
    private byte[] contentBytes;

    private String requestId;

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public HttpResponseStatus getHttpRspStatus() {
        return httpRspStatus;
    }

    public void setHttpRspStatus(HttpResponseStatus httpRspStatus) {
        this.httpRspStatus = httpRspStatus;
    }

    public List<Map.Entry<String, String>> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Map.Entry<String, String>> headers) {
        this.headers = headers;
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
