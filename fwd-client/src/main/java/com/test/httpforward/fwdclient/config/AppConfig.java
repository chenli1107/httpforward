package com.test.httpforward.fwdclient.config;

import com.test.fwdcommon.entity.req.ClientRegReq;
import com.test.fwdcommon.utils.SpringContextHolder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Data
@Configuration
@Import({SpringContextHolder.class})
public class AppConfig {
    @Value("${forward.netty.server.ip:127.0.0.1}")
    private String serverIp;
    @Value("${forward.netty.server.port:9000}")
    private int serverPort;

    private ClientRegReq clientRegReq;

    @Bean
    @ConfigurationProperties(prefix="httpurl.mapping")
    public ClientRegReq httpUrlMapping(){
        clientRegReq = new ClientRegReq();
        return clientRegReq;
    }

}
