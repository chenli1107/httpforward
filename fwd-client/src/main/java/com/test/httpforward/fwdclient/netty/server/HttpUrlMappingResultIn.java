package com.test.httpforward.fwdclient.netty.server;

import com.test.fwdcommon.entity.HttpUrlMapping;
import com.test.fwdcommon.entity.HttpUrlMappingResult;
import com.test.fwdcommon.utils.SpringContextHolder;
import com.test.httpforward.fwdclient.AppConfig;
import com.test.httpforward.fwdclient.netty.server.BaseInServer;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HttpUrlMappingResultIn implements BaseInServer<HttpUrlMappingResult> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AppConfig appConfig;

    @Override
    public boolean supports(Object msg) {
        return msg instanceof HttpUrlMappingResult;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpUrlMappingResult msg) throws Exception {
        logger.info("{}注册结果:{}", fullMapDesc(msg), mapper.writeValueAsString(msg));
    }

    private String fullMapDesc(HttpUrlMappingResult msg){
        if(!msg.isSuccess())return "";
        HttpUrlMapping mapping = msg.getMapping();
        String serverIp = appConfig.getServerIp();
        Integer serverPort = msg.getServerWebPort();
        return "["+ mapping.getRealWebServerHost()+mapping.getRealWebServerPath() +"]-->>["+serverIp +":"+ serverPort +mapping.getProxyServerPath()+"]";
    }
}
