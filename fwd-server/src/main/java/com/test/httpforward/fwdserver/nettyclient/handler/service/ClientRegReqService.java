package com.test.httpforward.fwdserver.nettyclient.handler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.fwdcommon.entity.req.ClientRegReq;
import com.test.fwdcommon.entity.rsp.ClientRegRsp;
import com.test.fwdcommon.face.BaseInServer;
import com.test.httpforward.fwdserver.channel.service.IChannelRegService;
import com.test.httpforward.fwdserver.urlReg.bean.UrlRegVo;
import com.test.httpforward.fwdserver.urlReg.service.IUrlRegService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientRegReqService implements BaseInServer<ClientRegReq> {
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    IUrlRegService urlRegService;
    @Autowired
    IChannelRegService channelRegService;

    @Override
    public boolean supports(Object msg) {
        return msg instanceof ClientRegReq;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ClientRegReq msg) throws Exception {
        UrlRegVo urlRegVo = urlRegService.getByCertificates(msg.getCertificates());
        ClientRegRsp rsp = null;
        if(urlRegVo==null){
            rsp = ClientRegRsp.builder()
                    .success(false)
                    .msg("建立连接失败。未知的凭证")
                    .build();
        }else{
            channelRegService.regist(msg.getCertificates(), ctx.channel());
            rsp = ClientRegRsp.builder()
                    .success(true)
                    .msg("建立连接成功")
                    .clientWebServerPath(urlRegVo.getDto().getClientWebServerPath())
                    .build();
        }
        ctx.channel().writeAndFlush(mapper.writeValueAsString(rsp));
    }
}
