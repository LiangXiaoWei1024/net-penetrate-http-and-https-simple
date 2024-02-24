package com.kele.penetrate;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ReferenceCountUtil;

public class Test3
{
    public static void main(String[] args)
    {
        int localPort = 8888;
        String remoteHost = "127.0.0.1";
        int remotePort = 9999;

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel ch)
                        {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new PortForwardHandler(remoteHost, remotePort));
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(localPort).sync();
            channelFuture.channel().closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static class PortForwardHandler extends ChannelInboundHandlerAdapter
    {
        private final String remoteHost;
        private final int remotePort;
        private Channel remoteChannel;

        public PortForwardHandler(String remoteHost, int remotePort)
        {
            this.remoteHost = remoteHost;
            this.remotePort = remotePort;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx)
        {
            Channel inboundChannel = ctx.channel();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new SimpleChannelInboundHandler<ByteBuf>()
                    {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx)
                        {
                            System.out.println("aaaaaaaaaa");
                            remoteChannel = ctx.channel();
                        }

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
                        {
                            ReferenceCountUtil.retain(msg);  // 保持引用计数，确保消息不会在传递过程中被释放
                            inboundChannel.writeAndFlush(msg);
                        }
                    });

            ChannelFuture future = bootstrap.connect(remoteHost, remotePort);
//            future.addListener((ChannelFutureListener) channelFuture ->
//            {
//                if (channelFuture.isSuccess())
//                {
//                    System.out.println("------------------");
//                    remoteChannel = ((ChannelFuture) channelFuture).channel();
//                }
//                else
//                {
//                    System.out.println("===========================");
//                    inboundChannel.close();
//                }
//            });
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
        {
            while (true){
                if (remoteChannel != null && remoteChannel.isActive())
                {
                    ByteBuf byteBuf = (ByteBuf) msg;
                    remoteChannel.writeAndFlush(Unpooled.copiedBuffer(byteBuf));
                    break;
                }else{
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }

        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx)
        {
            if (remoteChannel != null && remoteChannel.isActive())
            {
                remoteChannel.close();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
