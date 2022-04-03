package com.kele.penetrate.pojo;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class ServicePipeline
{
    private Object msg;
    private ChannelHandlerContext channelHandlerContext;

    public ServicePipeline(Object msg, ChannelHandlerContext channelHandlerContext)
    {
        this.msg = msg;
        this.channelHandlerContext = channelHandlerContext;
    }
}
