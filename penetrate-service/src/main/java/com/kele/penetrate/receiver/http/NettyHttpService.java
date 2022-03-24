package com.kele.penetrate.receiver.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@SuppressWarnings("unused")
public class NettyHttpService
{
    public NettyHttpService(int port)
    {
        init(port);
    }

    private void init(int port)
    {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap server = new ServerBootstrap();
            server.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyHttpChannelInitializerHandler());

            ChannelFuture future = server.bind(port).sync();
            System.out.println("http接收器启动成功，端口：" + port);
            future.channel().closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            System.out.println("启动失败");
            e.printStackTrace();
        }
        finally
        {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }
    }
}
