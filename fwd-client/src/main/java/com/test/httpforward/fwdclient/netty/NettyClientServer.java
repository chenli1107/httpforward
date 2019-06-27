package com.test.httpforward.fwdclient.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@Component
public class NettyClientServer implements ApplicationRunner {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ConnectionServer connectionServer;


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception{
        logger.info(">>>>>>>>>NettyClientServer init start<<<<<<<<<<<");
//        new Thread(() -> {
        connectionServer.connection();
//        }).start();
    }

}
