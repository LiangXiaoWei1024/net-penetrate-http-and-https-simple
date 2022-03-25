package com.kele.penetrate.service;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class NettyServiceChannelInitializerHandler extends ChannelInitializer<SocketChannel>
{
    @Override
    protected void initChannel(SocketChannel socketChannel)
    {
        socketChannel.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));
        socketChannel.pipeline().addLast(new ObjectEncoder());
        socketChannel.pipeline().addLast(new IdleStateHandler(30, 30, 30, TimeUnit.SECONDS));
//        socketChannel.pipeline().addLast(new SimpleServerHandler());
    }
}
