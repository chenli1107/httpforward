package com.test.httpforward.fwdserver.urlReg.bean;

import lombok.Data;

/**
 * @author chenl
 * @since 2020-08-22
 */
@Data
public class UrlRegDto {
    private String clientWebServerPath; // ip:port或ip:port/path 客户端web服务地址前缀，访问代理时，将会替换为此前缀路径
    private String proxyServerPath; //  /proxyServer fwd服务端个性化路径
}
