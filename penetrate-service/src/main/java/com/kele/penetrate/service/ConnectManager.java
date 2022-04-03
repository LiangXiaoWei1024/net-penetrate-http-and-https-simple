package com.kele.penetrate.service;

import com.kele.penetrate.factory.Recognizer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Data
@SuppressWarnings("unused")
@Recognizer
public class ConnectManager
{
    public final ConcurrentHashMap<ChannelId, ConnectHandler> channelIdBindConnectHandler = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, ConnectHandler> mappingNameBindConnectHandler = new ConcurrentHashMap<>();

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


}
