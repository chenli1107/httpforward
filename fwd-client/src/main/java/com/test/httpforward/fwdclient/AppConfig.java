package com.test.httpforward.fwdclient;

import com.test.fwdcommon.entity.HttpUrlMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${forward.netty.server.ip:127.0.0.1}")
    private String serverIp;
    @Value("${forward.netty.server.port:9000}")
    private int serverPort;

    /**
     * client目前无存储HttpUrlMapping的必要,主要由服务端映射转换url...所以一个client可支持多url映射
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix="httpurl.mapping")
    public HttpUrlMapping httpUrlMapping(){
        return new HttpUrlMapping();
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
