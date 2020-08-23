package com.test.httpforward.fwdserver.urlReg.service;

import com.test.fwdcommon.entity.ResultModel;
import com.test.fwdcommon.entity.enums.PubResultEnum;
import com.test.httpforward.fwdserver.channel.service.ChannelRegServiceImpl;
import com.test.httpforward.fwdserver.channel.service.IChannelRegService;
import com.test.httpforward.fwdserver.urlReg.bean.UrlRegDto;
import com.test.httpforward.fwdserver.urlReg.bean.UrlRegVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理url的映射
 */
@Slf4j
@Service
public class UrlRegServiceImpl implements IUrlRegService{
    @Autowired
    IChannelRegService channelRegService;

    //key:代理路径； value：代理具体信息
    private static ConcurrentHashMap<String, UrlRegVo> urlRegMap = new ConcurrentHashMap<>();

    @Override
    public UrlRegVo getByProxyServerPath(String proxyServerPath) {
        return urlRegMap.get(proxyServerPath);
    }

    @Override
    public UrlRegVo getByCertificates(String certificates){
        return urlRegMap.entrySet().stream()
                .filter(entry->certificates.equals(entry.getValue().getCertificates()))
                .findFirst()
                .map(entry->entry.getValue()).orElse(null);
    }

    @Override
    public ResultModel<UrlRegVo> regist(UrlRegDto dto) {
        UrlRegVo oldMapping = getByProxyServerPath(dto.getProxyServerPath());
        if(oldMapping != null) {
            return PubResultEnum.FAIL.msg("个性化地址已被注["+ dto.getProxyServerPath() +"]");
        }
        String certificates = UUID.randomUUID().toString();
//        certificates = "a1f1fdab-3340-412d-9455-f2939ccbf34b";
        UrlRegVo vo  = new UrlRegVo();
        vo.setDto(dto);
        vo.setCertificates(certificates);
        urlRegMap.put(dto.getProxyServerPath(), vo);
        return PubResultEnum.SUCC.data(vo);
    }

    @Override
    public void cancel(String certificates) {
        urlRegMap.entrySet().removeIf(entry -> entry.getValue().getCertificates().equals(certificates));
        Channel clientChannel = channelRegService.getByCertificates(certificates);
        if(clientChannel!=null){
            clientChannel.close();
        }
    }

    @Override
    public Collection<UrlRegVo> getAll() {
        return urlRegMap.values();
    }
}
