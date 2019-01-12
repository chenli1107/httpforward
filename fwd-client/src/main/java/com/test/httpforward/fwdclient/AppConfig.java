package com.test.httpforward.fwdclient;

import com.test.fwdcommon.entity.HttpUrlMapping;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     * client目前无存储HttpUrlMapping的必要,主要由服务端映射转换url...所以一个client可支持多url映射
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix="httpurl.mapping")
    public HttpUrlMapping httpUrlMapping(){
        return new HttpUrlMapping();
    }

}
