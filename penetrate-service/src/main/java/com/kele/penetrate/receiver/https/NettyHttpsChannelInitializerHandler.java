package com.kele.penetrate.receiver.https;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.net.ssl.SSLEngine;


@SuppressWarnings("unused")
@Recognizer
public class NettyHttpsChannelInitializerHandler extends ChannelInitializer<SocketChannel>
{

    @Autowired
    private NettyHttpsServerHandler nettyHttpsServerHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        //<editor-fold desc="生成本地证书(注释)">
//        SelfSignedCertificate certificate = new SelfSignedCertificate();
//        SslContext sslContext = SslContextBuilder.forServer(certificate.certificate(), certificate.privateKey()).build();
//        socketChannel.pipeline().addLast(sslContext.newHandler(socketChannel.alloc()));
        //</editor-fold>

        SSLEngine sslEngine = SSLContextFactory.getSslContext().createSSLEngine();
        sslEngine.setUseClientMode(false);
        socketChannel.pipeline().addLast(new SslHandler(sslEngine));

        // 添加一个HTTP的编解码器
        socketChannel.pipeline().addLast(new HttpServerCodec());
        // 将HTTP消息的多个部分合成一条完整的HTTP消息
        socketChannel.pipeline().addLast(new HttpObjectAggregator(52428800));
        // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
        // 自定义处理handler
        socketChannel.pipeline().addLast(nettyHttpsServerHandler);
    }
}
