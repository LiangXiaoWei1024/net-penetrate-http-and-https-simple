package com.kele.penetrate.service;

import com.kele.penetrate.Start;
import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.Recognizer;
import com.kele.penetrate.pojo.ServicePipeline;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@SuppressWarnings("unused")
@Recognizer
@ChannelHandler.Sharable
public class ServiceHandler extends SimpleChannelInboundHandler<Object>
{
    @Autowired
    private ConnectManager connectManager;


    //<editor-fold desc="读取通道消息">
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg)
    {
        Start.serviceEvents.notice(new ServicePipeline(msg,channelHandlerContext) );
    }
    //</editor-fold>

    //<editor-fold desc="通道激活">
    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
    }
    //</editor-fold>

    //<editor-fold desc="通道断开">
    @Override
    public void channelInactive(ChannelHandlerContext ctx)
    {
        connectManager.remove(ctx);
    }
    //</editor-fold>

    //<editor-fold desc="长时间没有通信">
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
    {
        ctx.flush();
        ctx.close();
    }
    //</editor-fold>
}
