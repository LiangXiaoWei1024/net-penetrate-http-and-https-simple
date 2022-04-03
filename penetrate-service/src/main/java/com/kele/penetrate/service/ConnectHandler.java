package com.kele.penetrate.service;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
@SuppressWarnings("unused")
@Data
public class ConnectHandler
{
    private final ChannelHandlerContext ctx;
    private String mappingName;
    private String mappingIp;
    private String port;

    public ConnectHandler(ChannelHandlerContext ctx)
    {
        this.ctx = ctx;
        InetSocketAddress insect = (InetSocketAddress) ctx.channel().remoteAddress();
    }

    //<editor-fold desc="发送消息">
    public void reply(Object msg)
    {
        ctx.writeAndFlush(msg);
    }
    //</editor-fold>

}
