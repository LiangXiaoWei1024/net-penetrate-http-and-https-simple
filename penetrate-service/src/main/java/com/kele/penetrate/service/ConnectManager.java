package com.kele.penetrate.service;

import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.protocol.BaseRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@SuppressWarnings("unused")
@Recognizer
public class ConnectManager
{
    private final ConcurrentHashMap<ChannelId, ConnectHandler> channelIdBindConnectHandler = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConnectHandler> mappingNameBindConnectHandler = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, MsgManager> requestIdBindConnect = new ConcurrentHashMap<>();

    public class MsgManager
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


    //<editor-fold desc="给所有用户发送消息">
    public void replyAll(Object msg)
    {
        channelIdBindConnectHandler.forEach((k, v) ->
                v.getCtx().writeAndFlush(msg));
    }
    //</editor-fold>

    public synchronized void add(ConnectHandler connectHandler)
    {
        channelIdBindConnectHandler.put(connectHandler.getCtx().channel().id(), connectHandler);
        mappingNameBindConnectHandler.put(connectHandler.getMappingName(), connectHandler);
        log.info("新的连接进来," + "共有" + channelIdBindConnectHandler.size() + "个连接,{" + connectHandler + "}");
    }

    public ConnectHandler get(String mappingName)
    {
        return mappingNameBindConnectHandler.get(mappingName);
    }

    public synchronized void remove(ChannelHandlerContext ctx)
    {
        ConnectHandler connectHandler = channelIdBindConnectHandler.get(ctx.channel().id());
        channelIdBindConnectHandler.remove(ctx.channel().id());
        mappingNameBindConnectHandler.remove(connectHandler.getMappingName());
        log.info("连接断开," + "共有" + channelIdBindConnectHandler.size() + "个连接,{" + connectHandler + "}");
    }

    public boolean isExist(String mappingName)
    {
        return mappingNameBindConnectHandler.containsKey(mappingName);
    }

    public synchronized void recordMsg(BaseRequest baseRequest, ChannelHandlerContext channelHandlerContext)
    {
        MsgManager msgManager = new MsgManager();
        msgManager.setChannelHandlerContext(channelHandlerContext);
        msgManager.setAddTime(System.currentTimeMillis());
        requestIdBindConnect.put(baseRequest.getRequestId(), msgManager);
    }

    public synchronized MsgManager getRecordMsg(String requestId)
    {
        MsgManager msgManager = requestIdBindConnect.get(requestId);
        requestIdBindConnect.remove(requestId);
        return msgManager;
    }

}
