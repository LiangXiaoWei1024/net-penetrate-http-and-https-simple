package com.kele.penetrate.pojo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;

@Data
public class PipelineTransmission
{
    private FullHttpRequest fullHttpRequest;
    private ChannelHandlerContext channelHandlerContext;

    public PipelineTransmission(ChannelHandlerContext channelHandlerContext,FullHttpRequest fullHttpRequest)
    {
        this.fullHttpRequest = fullHttpRequest;
        this.channelHandlerContext = channelHandlerContext;
    }
}
