package com.kele.penetrate.service.pipeline;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.ServicePipeline;
import com.kele.penetrate.protocol.Handshake;
import com.kele.penetrate.protocol.HandshakeResult;
import com.kele.penetrate.service.ConnectHandler;
import com.kele.penetrate.service.ConnectManager;
import com.kele.penetrate.utils.Func;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Register
@Recognizer
public class HandshakePipeline implements Func<ServicePipeline, Boolean>
{
    @Autowired
    private ConnectManager connectManager;

    @Override
    public Boolean func(ServicePipeline servicePipeline)
    {
        Object msg = servicePipeline.getMsg();
        ChannelHandlerContext channelHandlerContext = servicePipeline.getChannelHandlerContext();
        if (msg instanceof Handshake)
        {
            Handshake handshake = (Handshake) msg;
            if (connectManager.isExist(handshake.getMappingName()))
            {
                //映射名称已经存在
                channelHandlerContext.writeAndFlush(new HandshakeResult());
            }
            else
            {
                ConnectHandler.ConnectHandlerBuilder connectHandlerBuilder = ConnectHandler.builder();
                connectHandlerBuilder.ctx(channelHandlerContext);
                connectHandlerBuilder.mappingIp(handshake.getMappingIp());
                connectHandlerBuilder.port(handshake.getPort());
                connectHandlerBuilder.mappingName(handshake.getMappingName());
                ConnectHandler connectHandler = connectHandlerBuilder.build();
                connectManager.add(connectHandler);
                //通知服务端映射成功
                HandshakeResult handshakeResult = new HandshakeResult(true, "http(s)://xxx.xxx.xxx.com/" + handshake.getMappingName());
                connectHandler.reply(handshakeResult);
            }
            return true;
        }
        return false;
    }
}
