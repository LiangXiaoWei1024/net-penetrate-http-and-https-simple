package com.kele.penetrate.receiver.https;

import com.kele.penetrate.Start;
import com.kele.penetrate.enumeration.HypertextTransferProtocolType;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.pojo.PipelineTransmission;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;


@Recognizer
@SuppressWarnings("unused")
@ChannelHandler.Sharable
@Slf4j
public class NettyHttpsServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest)
    {
        Start.hypertextProtocolEvents.notice(new PipelineTransmission(channelHandlerContext, fullHttpRequest, HypertextTransferProtocolType.HTTPS));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if(!(cause instanceof DecoderException)){
            log.error("netty HTTPS 错误日志",cause);
        }
        ctx.flush();
        ctx.close();
    }

}
