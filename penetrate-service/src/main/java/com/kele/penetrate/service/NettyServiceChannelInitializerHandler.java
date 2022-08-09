package com.kele.penetrate.service;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@Recognizer
public class NettyServiceChannelInitializerHandler extends ChannelInitializer<SocketChannel>
{

    @Autowired
    private ServiceHandler serviceHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel)
    {
        socketChannel.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));
        socketChannel.pipeline().addLast(new ObjectEncoder());
        socketChannel.pipeline().addLast(new IdleStateHandler(60, 60, 60, TimeUnit.SECONDS));
        socketChannel.pipeline().addLast(serviceHandler);
    }
}
