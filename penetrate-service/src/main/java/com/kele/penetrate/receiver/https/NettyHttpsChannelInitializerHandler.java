package com.kele.penetrate.receiver.https;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.net.ssl.SSLEngine;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;


@SuppressWarnings("unused")
@Recognizer
public class NettyHttpsChannelInitializerHandler extends ChannelInitializer<SocketChannel>
{
    @Autowired
    private NettyHttpsServerHandler nettyHttpsServerHandler;
    @Autowired
    private SSLContextFactory sslContextFactory;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        //配置证书
        SslContext sslContext = sslContextFactory.getSslContext();
        socketChannel.pipeline().addLast(sslContext.newHandler(socketChannel.alloc()));
        // 添加一个HTTP的编解码器
        socketChannel.pipeline().addLast(new HttpServerCodec());
        // 将HTTP消息的多个部分合成一条完整的HTTP消息
        socketChannel.pipeline().addLast(new HttpObjectAggregator(104857600));
        // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
        // 自定义处理handler
        socketChannel.pipeline().addLast(nettyHttpsServerHandler);
    }
}
