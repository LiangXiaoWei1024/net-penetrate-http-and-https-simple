package com.kele.penetrate.service;

import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.Recognizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Recognizer
@SuppressWarnings("unused")
public class NettyServiceInit extends Thread
{
    @Autowired
    private Config config;

    @Override
    public void run()
    {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServiceChannelInitializerHandler());

            ChannelFuture future = server.bind(config.getServicePort()).sync();
            log.info("服务器启动成功,端口：" + config.getServicePort());
            future.channel().closeFuture().sync();
        }
        catch (Exception ex)
        {
            log.error("服务器启动失败", ex);
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
