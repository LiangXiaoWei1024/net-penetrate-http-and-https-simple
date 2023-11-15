package com.kele.penetrate.service;

import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.protocol.BaseRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@SuppressWarnings("unused")
@Recognizer
public class ConnectManager
{
    private final ConcurrentHashMap<ChannelId, ConnectHandler> channelIdBindConnectHandler = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, MessageManager> requestIdBindConnect = new ConcurrentHashMap<>();
    private final static Object CONNECT_LOCK = new Object();
    private final static Object MESSAGE_LOCK = new Object();
    private final static long THRESHOLD = 1000 * 60 * 5;

    public void replyAll(Object msg)
    {
        channelIdBindConnectHandler.forEach((k, v) ->
        {
            if (v.getCtx().channel().isActive())
            {
                v.getCtx().writeAndFlush(msg);
            }
        });
    }

    public void add(ConnectHandler connectHandler)
    {
        synchronized (CONNECT_LOCK)
        {
            channelIdBindConnectHandler.put(connectHandler.getCtx().channel().id(), connectHandler);
            log.info("新的连接进来,连接总数 [{}]", channelIdBindConnectHandler.size());
        }
    }

    public void remove(ChannelHandlerContext ctx)
    {
        synchronized (CONNECT_LOCK)
        {
            ConnectHandler connectHandler = channelIdBindConnectHandler.get(ctx.channel().id());
            channelIdBindConnectHandler.remove(ctx.channel().id());
            log.info("连接断开 [{}],连接总数 [{}]", connectHandler.getCustomDomainName(), channelIdBindConnectHandler.size());
        }
    }

    public ConnectHandler get(ChannelId channelId)
    {
        return channelIdBindConnectHandler.get(channelId);
    }

    public ConnectHandler get(String customDomainName)
    {
        ConnectHandler connectHandler = null;
        Set<Map.Entry<ChannelId, ConnectHandler>> entries = channelIdBindConnectHandler.entrySet();
        Iterator<Map.Entry<ChannelId, ConnectHandler>> iterator = entries.stream().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<ChannelId, ConnectHandler> next = iterator.next();
            ConnectHandler value = next.getValue();
            if (value.getCustomDomainName() == null)
            {
                continue;
            }
            if (value.getCustomDomainName().equals(customDomainName))
            {
                connectHandler = value;
                break;
            }
        }
        return connectHandler;
    }

    public boolean isExist(String customDomainName)
    {
        boolean result = false;
        Set<Map.Entry<ChannelId, ConnectHandler>> entries = channelIdBindConnectHandler.entrySet();
        Iterator<Map.Entry<ChannelId, ConnectHandler>> iterator = entries.stream().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<ChannelId, ConnectHandler> next = iterator.next();
            ConnectHandler connectHandler = next.getValue();
            if (connectHandler.getCustomDomainName() == null)
            {
                continue;
            }
            if (connectHandler.getCustomDomainName().equals(customDomainName))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public void addRecordMessage(BaseRequest baseRequest, ChannelHandlerContext channelHandlerContext)
    {
        synchronized (MESSAGE_LOCK)
        {
            MessageManager msgManager = new MessageManager();
            msgManager.setChannelHandlerContext(channelHandlerContext);
            msgManager.setAddTime(System.currentTimeMillis());
            requestIdBindConnect.put(baseRequest.getRequestId(), msgManager);
        }
    }

    public void removeRecordMessage(String requestId)
    {
        synchronized (MESSAGE_LOCK)
        {
            requestIdBindConnect.remove(requestId);
        }
    }

    public MessageManager getRecordMessage(String requestId)
    {
        return requestIdBindConnect.get(requestId);
    }

    public void clearUntreatedMessage()
    {
        synchronized (MESSAGE_LOCK)
        {
            Set<Map.Entry<String, MessageManager>> entries = requestIdBindConnect.entrySet();
            Iterator<Map.Entry<String, MessageManager>> iterator = entries.stream().iterator();
            while (iterator.hasNext())
            {
                Map.Entry<String, MessageManager> next = iterator.next();
                if (System.currentTimeMillis() - next.getValue().getAddTime() >= THRESHOLD)
                {
                    requestIdBindConnect.remove(next.getKey());
                }
            }
        }
    }

    public static class MessageManager
    {
        private ChannelHandlerContext channelHandlerContext;
        private long addTime;

        public ChannelHandlerContext getChannelHandlerContext()
        {
            return channelHandlerContext;
        }

        public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext)
        {
            this.channelHandlerContext = channelHandlerContext;
        }

        public long getAddTime()
        {
            return addTime;
        }

        public void setAddTime(long addTime)
        {
            this.addTime = addTime;
        }
    }
}
