package com.test.httpforward.fwdserver.server;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Service
public class HttpRequestCacheManager {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${http.netty.web.request.timeout:3000}")
    private long requestTimeout;

    //TODO 防止部分代理连接流量过大,可考虑根据client.channel分别存储限制大小, 或直接client.channel.attr(key)中存储限容量
    //存储requestId，接受到响应通过channel返回给web用户
    private ConcurrentHashMap<String, RequestDelayedMessage> requestChannelMap = new ConcurrentHashMap();
    //清除超时没收到response的request
    private DelayQueue<RequestDelayedMessage> delayQueue = new DelayQueue<>();


    @PostConstruct
    public void startClear(){
        new Thread(()->{
            while(true){
                try {
                    RequestDelayedMessage req =  delayQueue.take();
                    requestChannelMap.remove(req.requestId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void put(String requestId, Channel channel) {
        RequestDelayedMessage req = new RequestDelayedMessage(requestId, channel, requestTimeout);
        requestChannelMap.put(requestId, req);
        delayQueue.offer(req);
    }

    public Channel remove(String requestId) {
        RequestDelayedMessage req = requestChannelMap.remove(requestId);
        if(req==null)
            return null;
        delayQueue.remove(req);
        return req == null ? null : req.channel;
    }

}
class RequestDelayedMessage implements Delayed {
    public final String requestId;
    public final Channel channel;
    public final long delay; //延迟时间。即等待时间
    public final long expire;  // 到期时间

    public RequestDelayedMessage(String requestId, Channel channel, long delay) {
        this.requestId = requestId;
        this.channel = channel;
        this.delay = delay;
        expire = System.currentTimeMillis() + delay;    //到期时间 = 当前时间+延迟时间
    }

    /**
     * 剩余延迟时间.用过期时间-当前时间
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis() , TimeUnit.MILLISECONDS);
    }

    /**
     * 内部比较排序   当前时间的延迟时间 - 比较对象的延迟时间
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) -o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "RequestDelayedMessage{" +
                "requestId='" + requestId + '\'' +
                ", channel=" + channel +
                ", delay=" + delay +
                ", expire=" + expire +
                '}';
    }
}