package com.kele.penetrate.client;

import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.page.ClientLogPageManager;
import com.kele.penetrate.protocol.Handshake;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Data
@Slf4j
@Recognizer
@SuppressWarnings("unused")
public class ConnectHandler
{
    //<editor-fold desc="属性">
    private Bootstrap bootstrap;
    private Channel channel;
    private long lastReplyTime;
    private Handshake handshake;
    //</editor-fold>

    //<editor-fold desc="注入">
    @Autowired
    private NettyClientChannelInitializerHandler nettyClientChannelInitializerHandler;
    @Autowired
    private Config config;
    @Autowired
    private ClientLogPageManager clientLogPageManager;
    //</editor-fold>

    //<editor-fold desc="启动">
    public void start()
    {
        bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.handler(nettyClientChannelInitializerHandler);
        doConnect();
    }
    //</editor-fold>

    //<editor-fold desc="连接">
    protected void doConnect()
    {
        if (this.channel != null && this.channel.isActive())
            return;
        ChannelFuture future = bootstrap.connect(config.getServiceConnectInfo().getIp(), config.getServiceConnectInfo().getPort());

        future.addListener((ChannelFutureListener) futureListener ->
        {
            if (futureListener.isSuccess())
            {
                setChannel(future.channel());
                clientLogPageManager.addLog("连接成功");
                log.info("与服务器连接成功：{} : {}", config.getServiceConnectInfo().getIp(), config.getServiceConnectInfo().getPort());
            }
            else
            {
                clientLogPageManager.addLog("5秒后自动重连...");
                log.info("5秒后自动重连：{} : {}", config.getServiceConnectInfo().getIp(), config.getServiceConnectInfo().getPort());
                futureListener.channel().eventLoop().schedule(this::doConnect, 5, TimeUnit.SECONDS);
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="是否连接成功">
    public boolean isConnect()
    {
        if (channel == null)
        {
            return false;
        }
        return channel.isActive();
    }
    //</editor-fold>

    //<editor-fold desc="连接断开处理">
    public void disconnect()
    {
        log.info("连接断开");
        clientLogPageManager.addLog("连接断开(自动重联～)");
        setChannel(null);
        doConnect();
    }
    //</editor-fold>

    //<editor-fold desc="发送消息">
    public void send(Object msg)
    {
        if (this.channel == null || !this.channel.isActive())
            return;
        this.channel.writeAndFlush(msg);
    }
    //</editor-fold>
}
