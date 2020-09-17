package com.test.httpforward.fwdserver.nettyclient.handler.service;

import com.test.httpforward.fwdserver.config.AppConfig;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class HttpRequestCacheManager {
    @Autowired
    AppConfig appConfig;

    //TODO 防止部分代理连接流量过大,可考虑根据client.channel分别存储限制大小, 或直接client.channel.attr(key)中存储限容量
    //存储requestId，接受到响应通过channel返回给web用户
    public static ConcurrentHashMap<String, RequestDelayedMessage> requestChannelMap = new ConcurrentHashMap();
    //清除超时没收到response的request
    private static DelayQueue delayQueue = new DelayQueue<>();

    //剔除超时的request
    private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
            delayQueue,
            new ThreadFactory() {
                AtomicInteger a = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "rrrr-Thread-"+ a.getAndAdd(1));
                    thread.setDaemon(false);
                    return thread;
                }
            }
    );



    public void put(String requestId, Channel channel) {
        RequestDelayedMessage req = new RequestDelayedMessage(requestId, channel, appConfig.getRequestTimeout());
        requestChannelMap.put(requestId, req);
        delayQueue.put(req);
    }

    public Channel remove(String requestId) {
        RequestDelayedMessage req = requestChannelMap.remove(requestId);
        if(req==null)
            return null;
        delayQueue.remove(req);
        return req.channel;
    }

}
class RequestDelayedMessage implements Delayed, Runnable {
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

    @Override
    public void run() {
        HttpRequestCacheManager.requestChannelMap.remove(requestId);
    }
}