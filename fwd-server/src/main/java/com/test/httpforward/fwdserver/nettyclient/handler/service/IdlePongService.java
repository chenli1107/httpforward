package com.test.httpforward.fwdserver.nettyclient.handler.service;

import com.test.fwdcommon.entity.rsp.IdlePongEntity;
import com.test.fwdcommon.face.BaseInServer;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IdlePongService implements BaseInServer<IdlePongEntity> {


    @Override
    public boolean supports(Object msg) {
        return msg instanceof IdlePongEntity;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, IdlePongEntity msg) throws Exception {
        log.info("receive pong channel:{}, id:{}", ctx.channel(), msg.getIdelPing().getPingId());
    }
}
