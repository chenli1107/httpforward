package com.test.fwdcommon.entity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HttpRequestEntity extends BaseEntity{
    private String url;
    private String methodName;
    private List<Map.Entry<String, String>> headers;
    private byte[] contentBytes;
    private String requestId;
    private Date requestTime;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    @Override
    public String toString() {
        return "HttpRequestEntity{" +
                "url='" + url + '\'' +
                ", headers=" + headers +
                ", contentBytes=" + Arrays.toString(contentBytes) +
                ", requestId='" + requestId + '\'' +
                ", requestTime=" + requestTime +
                '}';
    }
}
