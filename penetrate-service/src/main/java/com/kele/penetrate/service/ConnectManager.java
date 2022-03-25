package com.kele.penetrate.service;

import io.netty.channel.ChannelId;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Data
@SuppressWarnings("unused")
public class ConnectManager
{
    private final ConcurrentHashMap<ChannelId,ConnectHandler> a = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String,ChannelId> b = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ChannelId,String> c = new ConcurrentHashMap<>();

}
