package com.test.httpforward.fwdserver;

import com.test.httpforward.fwdserver.nettyclient.NettyServer;
import com.test.httpforward.fwdserver.nettyweb.NettyHttpServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

//    @Bean(initMethod="serverStart")
////    @DependsOn({"httpURLMappingManager"})
//    public NettyServer nettyServer(){
//        return new NettyServer();
//    }
//
//    @Bean(initMethod="serverStart")
//    public NettyHttpServer nettyHttpServer(){
//        return new NettyHttpServer();
//    }


//    @Bean
//    public HttpURLMappingManager httpURLMappingManager(){
//        return new HttpURLMappingManager();
//    }
}
