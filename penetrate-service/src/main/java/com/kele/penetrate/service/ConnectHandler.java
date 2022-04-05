package com.kele.penetrate.service;

import io.netty.channel.ChannelHandlerContext;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@SuppressWarnings("unused")
@Data
@Builder
public class ConnectHandler
{
    private ChannelHandlerContext ctx;
    private String mappingName;
    private String mappingIp;
    private int port;
    private boolean isFilterMappingName;

    //<editor-fold desc="发送消息">
    public void reply(Object msg)
    {
        ctx.writeAndFlush(msg);
    }
    //</editor-fold>

    @Override
    public String toString()
    {
        return "ConnectHandler{" +
                "mappingName='" + mappingName + '\'' +
                ", mappingIp='" + mappingIp + '\'' +
                ", port=" + port +
                '}';
    }
}
