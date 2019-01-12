package com.test.httpforward.fwdserver.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.fwdcommon.entity.HttpUrlMapping;
import com.test.fwdcommon.entity.HttpUrlMappingResult;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理客户端url的映射
 */
//单例
public class HttpURLMappingManager {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ConcurrentHashMap<String, HttpUrlMapping> urlMap = null;

    private final static class ManagerBuilder {
        private static final HttpURLMappingManager manager = new HttpURLMappingManager();
    }

    private HttpURLMappingManager() {
        this.urlMap = new ConcurrentHashMap<>();
    }

    public static HttpURLMappingManager instance() {
        return ManagerBuilder.manager;
    }

    public HttpUrlMapping getHttpUrlMapping(String proxyServerPath) {
        return urlMap.get(proxyServerPath);
    }

    public synchronized HttpUrlMappingResult regist(HttpUrlMapping mapping) {
        HttpUrlMappingResult res = new HttpUrlMappingResult();
        res.setMapping(mapping);
        HttpUrlMapping oldMapping = getHttpUrlMapping(mapping.getProxyServerPath());
        if(oldMapping == null) {
            urlMap.put(mapping.getProxyServerPath(), mapping);
            res.setSuccess(true);
            res.setMsg("注册成功");
            logger.warn("client regist: {}", res);
            return res;
        }
        if(!oldMapping.getProxyClientChannel().equals(mapping.getProxyClientChannel())){
            res.setSuccess(false);
            res.setMsg("个性化地址已被注");
            logger.warn("client regist: {}", res);
            return res;
        }
        //更新real地址。。mapping.setRealWebServerPath();mapping.setRealWebServerHost();
        urlMap.put(mapping.getProxyServerPath(), mapping);
        res.setSuccess(true);
        res.setMsg("注册成功");
        logger.warn("client regist: {}", res);
        return res;
    }

    //通道断开时
    public void cancel(Channel channel) {
        Iterator<Map.Entry<String, HttpUrlMapping>> ite = urlMap.entrySet().iterator();
        while(ite.hasNext()){
            Map.Entry<String, HttpUrlMapping> ent = ite.next();
            if(channel.equals(ent.getValue().getProxyClientChannel())){
                ite.remove();
                logger.warn("client cancel: {}", ent.getValue());
            }
        }
    }

}
