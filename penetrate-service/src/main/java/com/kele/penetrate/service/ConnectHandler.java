package com.kele.penetrate.service;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@SuppressWarnings("unused")
@Data
public class ConnectHandler
{
    private ChannelHandlerContext ctx;
    private String customDomainName;

    public ConnectHandler(ChannelHandlerContext ctx)
    {
        this.ctx = ctx;
    }

    public void reply(Object msg)
    {
        if (ctx.channel().isActive())
        {
            ctx.writeAndFlush(msg);
        }
    }

    @Override
    public String toString()
    {
        return "ConnectHandler{" +
                "customDomainName='" + customDomainName + '\'' +
                '}';
    }
}
