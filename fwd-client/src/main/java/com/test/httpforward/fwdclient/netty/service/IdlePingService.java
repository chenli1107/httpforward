package com.test.httpforward.fwdclient.netty.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.fwdcommon.entity.req.IdlePingEntity;
import com.test.fwdcommon.entity.rsp.IdlePongEntity;
import com.test.fwdcommon.face.BaseInServer;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class IdlePingService implements BaseInServer<IdlePingEntity> {
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean supports(Object msg) {
        return msg instanceof IdlePingEntity;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, IdlePingEntity msg) throws Exception {
        IdlePongEntity pong = new IdlePongEntity();
        pong.setPongTime(new Date());
        pong.setIdelPing(msg);
        ctx.channel().writeAndFlush(mapper.writeValueAsString(pong));
    }
}
