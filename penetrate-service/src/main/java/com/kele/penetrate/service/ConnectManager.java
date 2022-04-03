package com.kele.penetrate.service;

import com.kele.penetrate.factory.Recognizer;
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
    public final ConcurrentHashMap<String, ChannelId> mappingNameBindChannelId = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<ChannelId, String> channelIdBindMappingName = new ConcurrentHashMap<>();

    //<editor-fold desc="发送消息">
    public void replyAll(Object msg)
    {
        channelIdBindConnectHandler.forEach((k, v) ->
                v.getCtx().writeAndFlush(msg));
    }
    //</editor-fold>

}
