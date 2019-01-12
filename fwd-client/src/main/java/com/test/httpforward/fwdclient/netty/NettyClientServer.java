package com.test.httpforward.fwdclient.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@Component
public class NettyClientServer {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ConnectionServer connectionServer;


    @PostConstruct
    public void serverStart() {
        logger.info(">>>>>>>>>NettyClientServer init start<<<<<<<<<<<");
//        new Thread(() -> {
        connectionServer.connection();
//        }).start();
    }

}
