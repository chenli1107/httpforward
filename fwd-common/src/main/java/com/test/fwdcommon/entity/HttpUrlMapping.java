package com.test.fwdcommon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;

public class HttpUrlMapping extends BaseEntity{
    private String realWebServerHost; // ip:port
    private String realWebServerPath; // /server
    private String proxyServerPath; //  /proxyServer
    @JsonIgnore
    private Channel proxyClientChannel; //客户端的通道.

    public String getRealWebServerHost() {
        return realWebServerHost;
    }

    public void setRealWebServerHost(String realWebServerHost) {
        this.realWebServerHost = realWebServerHost;
    }

    public String getRealWebServerPath() {
        return realWebServerPath;
    }

    public void setRealWebServerPath(String realWebServerPath) {
        this.realWebServerPath = realWebServerPath;
    }

    public Channel getProxyClientChannel() {
        return proxyClientChannel;
    }

    public void setProxyClientChannel(Channel proxyClientChannel) {
        this.proxyClientChannel = proxyClientChannel;
    }

    public String getProxyServerPath() {
        return proxyServerPath;
    }

    public void setProxyServerPath(String proxyServerPath) {
        this.proxyServerPath = proxyServerPath;
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
