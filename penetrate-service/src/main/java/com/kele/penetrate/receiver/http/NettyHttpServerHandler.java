package com.kele.penetrate.receiver.http;

import com.kele.penetrate.Start;
import com.kele.penetrate.enumeration.HypertextTransferProtocolType;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.pojo.PipelineTransmission;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;


@Recognizer
@SuppressWarnings("unused")
@ChannelHandler.Sharable
@Slf4j
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest)
    {
        Start.hypertextProtocolEvents.notice(new PipelineTransmission(channelHandlerContext, fullHttpRequest, HypertextTransferProtocolType.HTTP));
    }
}
