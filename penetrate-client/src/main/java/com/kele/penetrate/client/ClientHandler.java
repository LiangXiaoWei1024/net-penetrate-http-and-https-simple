package com.kele.penetrate.client;


import com.kele.penetrate.Start;
import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.Recognizer;
import com.kele.penetrate.protocol.Handshake;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


@SuppressWarnings("unused")
@Slf4j
@Recognizer
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<Object>
{
    @Autowired
    private ConnectHandler connectHandler;

    //<editor-fold desc="接收通道消息">
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
    {
        Start.clientEvents.notice(msg);
    }
    //</editor-fold>

    //<editor-fold desc="通道连接激活">
    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        connectHandler.setChannel(ctx.channel());
        //<editor-fold desc="通道激活与客户端握手，告知转发信息">
        Handshake handshake = new Handshake();
        handshake.setMappingIp("127.0.0.1");
        handshake.setPort(80);
        handshake.setMappingName("aaa");
        connectHandler.send(handshake);
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold desc="通道断开">
    @Override
    public void channelInactive(ChannelHandlerContext ctx)
    {
        connectHandler.disconnect();
    }
    //</editor-fold>

    //<editor-fold desc="长时间没有通信">
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
    {
        ctx.flush();
        ctx.close();
        System.out.println("aaaaaaa");
    }
    //</editor-fold>

}
