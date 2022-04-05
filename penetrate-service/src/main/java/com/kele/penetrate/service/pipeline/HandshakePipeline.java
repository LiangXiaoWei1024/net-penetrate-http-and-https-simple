package com.kele.penetrate.service.pipeline;

import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.ServicePipeline;
import com.kele.penetrate.pojo.VersionInfo;
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
    @Autowired
    private Config config;

    @Override
    public Boolean func(ServicePipeline servicePipeline)
    {
        Object msg = servicePipeline.getMsg();
        ChannelHandlerContext channelHandlerContext = servicePipeline.getChannelHandlerContext();
        if (msg instanceof Handshake)
        {
            Handshake handshake = (Handshake) msg;
            VersionInfo versionInfo = config.getVersionInfo();
            HandshakeResult handshakeResult = new HandshakeResult();
            boolean success;

            if (!versionInfo.getVersion().equals(handshake.getVersion()))
            {
                handshakeResult.setFailMessage(handshakeResult.getFailMessage() + "- 版本不一致、要求更新后使用、地址:https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple \r\n");
                handshakeResult.setFailMessage(handshakeResult.getFailMessage() + versionInfo.getContent());
            }
            else
            {
                if (connectManager.isExist(handshake.getMappingName()))
                {
                    //映射名称已经存在
                    channelHandlerContext.writeAndFlush(new HandshakeResult());
                    handshakeResult.setFailMessage("- 映射名称已存在(" + handshake.getMappingName() + ") \r\n");
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
                    //映射成功
                    handshakeResult.setSuccess(true);
                    handshakeResult.setAccessAddress("http(s)://xxx.xxx.xxx.com/" + handshake.getMappingName());
                }
            }
            channelHandlerContext.writeAndFlush(handshakeResult);
            return true;
        }
        return false;
    }
}
