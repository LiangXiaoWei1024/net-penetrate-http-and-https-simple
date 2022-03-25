package com.kele.penetrate.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@SuppressWarnings("unused")
public class ServiceHandler extends SimpleChannelInboundHandler<JSONObject>
{
    //<editor-fold desc="读取通道消息">
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject msg)
    {
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
