package com.kele.penetrate.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class NettyClientChannelInitializerHandler extends ChannelInitializer<NioSocketChannel>
{
    @Override
    protected void initChannel(NioSocketChannel ch)
    {
        ch.pipeline().addLast(new ObjectEncoder());
        ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null))); // 最大长度
        ch.pipeline().addLast(new IdleStateHandler(30, 30, 30));
//        ch.pipeline().addLast(new ClientHandler());
    }
}
