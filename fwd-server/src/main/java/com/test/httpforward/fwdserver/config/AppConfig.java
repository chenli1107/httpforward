package com.test.httpforward.fwdserver.config;

import com.test.fwdcommon.utils.SpringContextHolder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Import({SpringContextHolder.class})
@Getter
public class AppConfig {
    @Value("${http.netty.web.port}")
    private int webPort;

    @Value("${forward.netty.server.port}")
    private int serverPort;

    @Value("${http.netty.web.request.timeout:3000}")
    private long requestTimeout;

}
