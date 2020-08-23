package com.test.httpforward.fwdserver.channel.service;

import com.test.fwdcommon.entity.ResultModel;
import com.test.fwdcommon.entity.enums.PubResultEnum;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ChannelRegServiceImpl implements IChannelRegService {
    private static ConcurrentHashMap<String, Channel> channelRegMap = new ConcurrentHashMap<>();

    public Channel getByCertificates(String certificates) {
        return channelRegMap.get(certificates);
    }

    public ResultModel<Void> regist(String certificates, Channel channel) {
        //TODO 体现channel被替换；并关闭原channel
        channelRegMap.put(certificates, channel);
        return PubResultEnum.SUCC.msg();
    }

    public void destroy(Channel channel) {
        channelRegMap.entrySet().removeIf(entry -> entry.getValue().equals(channel));
    }
}
