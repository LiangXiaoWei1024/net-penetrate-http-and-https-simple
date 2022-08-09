package com.kele.penetrate.receiver.http;

import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Recognizer
@SuppressWarnings("unused")
@Slf4j
public class NettyHttpService extends Thread
{
    @Autowired
    private Config config;
    @Autowired
    private NettyHttpChannelInitializerHandler nettyHttpChannelInitializerHandler;

    @Override
    public void run()
    {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap server = new ServerBootstrap();
            server.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(nettyHttpChannelInitializerHandler);

            ChannelFuture future = server.bind(config.getHttpPort()).sync();
            log.info("http接收器启动成功：{}", config.getHttpPort());
            future.channel().closeFuture().sync();
        }
        catch (InterruptedException ex)
        {
            log.error("http启动失败", ex);
        }
        finally
        {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }
    }
}
