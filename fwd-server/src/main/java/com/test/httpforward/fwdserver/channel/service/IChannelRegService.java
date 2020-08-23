package com.test.httpforward.fwdserver.channel.service;

import com.test.fwdcommon.entity.ResultModel;
import io.netty.channel.Channel;

/**
 * @author chenl
 * @since 2020-08-23
 */
public interface IChannelRegService {

    Channel getByCertificates(String certificates);

    /**
     * 注册
     * @param certificates
     * @param channel
     * @return
     */
    ResultModel<Void> regist(String certificates, Channel channel);

    /**
     * 注销
     * @param channel
     */
    void destroy(Channel channel);
}
