package com.kele.penetrate.client;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


@SuppressWarnings("unused")
@Recognizer
public class NettyClientChannelInitializerHandler extends ChannelInitializer<NioSocketChannel>
{
    @Autowired
    private ClientHandler clientHandler;

    @Override
    protected void initChannel(NioSocketChannel ch)
    {
        ch.pipeline().addLast(new ObjectEncoder());
        ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null))); // 最大长度
        ch.pipeline().addLast(new IdleStateHandler(5, 5, 5, TimeUnit.MINUTES));
        ch.pipeline().addLast(clientHandler);
    }
}
