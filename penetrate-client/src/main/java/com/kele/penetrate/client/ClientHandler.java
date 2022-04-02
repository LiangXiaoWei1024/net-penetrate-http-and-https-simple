package com.kele.penetrate.client;


import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.Recognizer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


@SuppressWarnings("unused")
@Slf4j
@Recognizer
public class ClientHandler extends SimpleChannelInboundHandler<Object>
{
    @Autowired
    private ConnectHandler connectHandler;

    //<editor-fold desc="接收通道消息">
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
    {

    }
    //</editor-fold>

    //<editor-fold desc="通道连接激活">
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
