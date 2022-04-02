package com.kele.penetrate.client;

import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.Recognizer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;


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
        ch.pipeline().addLast(new IdleStateHandler(30, 30, 30));
        ch.pipeline().addLast(clientHandler);
    }
}
