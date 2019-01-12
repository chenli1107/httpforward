package com.test.httpforward.fwdserver.nettyclient.server;

import com.test.fwdcommon.entity.IdlePingEntity;
import com.test.fwdcommon.entity.IdlePongEntity;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class IdlePongIn implements BaseInServer<IdlePongEntity> {
    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public boolean supports(Object msg) {
        return msg instanceof IdlePongEntity;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, IdlePongEntity msg) throws Exception {
        logger.info("receive pong channel:{}, id:{}", ctx.channel(), msg.getIdelPing().getPingId());
    }
}
