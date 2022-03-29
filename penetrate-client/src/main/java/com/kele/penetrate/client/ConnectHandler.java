//package com.kele.penetrate.client;
//
//import com.alibaba.fastjson.JSONObject;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.serialization.ClassResolvers;
//import io.netty.handler.codec.serialization.ObjectDecoder;
//import io.netty.handler.codec.serialization.ObjectEncoder;
//import io.netty.handler.timeout.IdleStateHandler;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.TimeUnit;
//
//@Data
//@Slf4j
//@SuppressWarnings("unused")
//public class ConnectHandler
//{
//    private Bootstrap bootstrap;
//    private Channel channel;
//    private long lastReplyTime;
//    private ConcurrentHashMap<String, JSONObject> monitoringDataMap = new ConcurrentHashMap<>();
//
//
//    //<editor-fold desc="构造">
//    public ConnectHandler()
//    {
//    }
//    //</editor-fold>
//
//    //<editor-fold desc="启动">
//    public void start()
//    {
//        bootstrap = new Bootstrap();
//        EventLoopGroup group = new NioEventLoopGroup();
//
//        bootstrap.group(group);
//        bootstrap.channel(NioSocketChannel.class);
//        bootstrap.option(ChannelOption.TCP_NODELAY, true);
//
//        //第3步 给NIoSocketChannel初始化handler， 处理读写事件
//        bootstrap.handler(new ChannelInitializer<NioSocketChannel>()
//        {  //通道是NioSocketChannel
//            @Override
//            protected void initChannel(NioSocketChannel ch)
//            {
//                ch.pipeline().addLast(new ObjectEncoder());
//                ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null))); // 最大长度
//                ch.pipeline().addLast(new IdleStateHandler(10, 10, 10));
//                ch.pipeline().addLast(new com.bp.local.client.SimpleClientHandler());
//            }
//        });
//        doConnect();
//    }
//    //</editor-fold>
//
//    //<editor-fold desc="连接">
//    protected void doConnect()
//    {
//        if (this.channel != null && this.channel.isActive())
//            return;
//        ChannelFuture future = bootstrap.connect(ip, port);
//
//        future.addListener((ChannelFutureListener) futureListener ->
//        {
//            if (futureListener.isSuccess())
//            {
//                this.channel = futureListener.channel();
//                log.info("与服务器连接成功：" + ip + ":" + port);
//            }
//            else
//            {
//                log.info("5秒之后自动重连：" + ip + ":" + port);
//                futureListener.channel().eventLoop().schedule(this::doConnect, 5, TimeUnit.SECONDS);
//            }
//        });
//    }
//    //</editor-fold>
//
//    //<editor-fold desc="发送消息">
//    public void send(String msg)
//    {
//        if (this.channel == null || !this.channel.isActive())
//            return;
//
//        this.channel.writeAndFlush(msg);
//    }
//    //</editor-fold>
//}
