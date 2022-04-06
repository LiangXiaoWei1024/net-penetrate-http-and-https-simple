package com.kele.penetrate.pojo;

import com.kele.penetrate.enumeration.HypertextTransferProtocolType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;

@Data
@SuppressWarnings("unused")
public class PipelineTransmission
{
    private FullHttpRequest fullHttpRequest;
    private ChannelHandlerContext channelHandlerContext;
    private HypertextTransferProtocolType hypertextTransferProtocolType;

    public PipelineTransmission(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest, HypertextTransferProtocolType hypertextTransferProtocolType)
    {
        this.fullHttpRequest = fullHttpRequest;
        this.channelHandlerContext = channelHandlerContext;
        this.hypertextTransferProtocolType = hypertextTransferProtocolType;
    }
}
