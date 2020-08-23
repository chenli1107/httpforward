package com.test.httpforward.fwdclient.netty.service;

import com.test.fwdcommon.entity.rsp.ClientRegRsp;
import com.test.fwdcommon.face.BaseInServer;
import com.test.httpforward.fwdclient.config.AppConfig;
import com.test.httpforward.fwdclient.netty.ConnectionServer;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientRegRspService implements BaseInServer<ClientRegRsp> {
    @Autowired
    AppConfig appConfig;
    @Autowired
    ConnectionServer connectionServer;

    @Override
    public boolean supports(Object msg) {
        return msg instanceof ClientRegRsp;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ClientRegRsp msg) throws Exception {
        if(msg.isSuccess()){
            log.info("client reg success,clientWebServerPath[{}]........", msg.getClientWebServerPath());
        }else{
            log.error("client reg fail!!!!!!!Certificates:"+appConfig.getClientRegReq().getCertificates());
            connectionServer.destory();
        }
    }
}
