package com.kele.penetrate.receiver.https;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;



@SuppressWarnings("unused")
public class NettyHttpsService
{
    public NettyHttpsService(int port)
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
                    .childHandler(new NettyHttpsChannelInitializerHandler());


            ChannelFuture future = server.bind(port).sync();
            System.out.println("https接收器启动成功，端口：" + port);
            future.channel().closeFuture().sync();
        }
        catch (Exception ex)
        {
            System.out.println("启动失败");
            ex.printStackTrace();
        }
        finally
        {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }
    }
}
