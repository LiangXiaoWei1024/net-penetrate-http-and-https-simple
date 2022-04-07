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

import java.util.ArrayList;
import java.util.List;

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
            List<String> failMessages = new ArrayList<>();
            handshakeResult.setFailMessages(failMessages);
            boolean success;

            if (!versionInfo.getVersion().equals(handshake.getVersion()))
            {
                failMessages.add("版本不一致、要求更新后使用、下载地址:https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple");
                for (int i = 0; i < versionInfo.getContents().size(); i++)
                {
                    failMessages.add(versionInfo.getContents().getString(i));
                }
            }
            else
            {
                if (connectManager.isExist(handshake.getMappingName()))
                {
                    //映射名称已经存在
                    channelHandlerContext.writeAndFlush(new HandshakeResult());
                    failMessages.add("映射名称已存在(" + handshake.getMappingName() + ")");
                }
                else
                {
                    ConnectHandler.ConnectHandlerBuilder connectHandlerBuilder = ConnectHandler.builder();
                    connectHandlerBuilder.ctx(channelHandlerContext);
                    connectHandlerBuilder.mappingIp(handshake.getMappingIp());
                    connectHandlerBuilder.port(handshake.getPort());
                    connectHandlerBuilder.mappingName(handshake.getMappingName());
                    connectHandlerBuilder.isFilterMappingName(handshake.isFilterMappingName());
                    ConnectHandler connectHandler = connectHandlerBuilder.build();
                    connectManager.add(connectHandler);
                    //映射成功
                    handshakeResult.setSuccess(true);
                    if (handshake.isFilterMappingName())
                    {
                        failMessages.add("http://101.35.221.134:" + config.getHttpPort() + "/" + handshake.getMappingName() + "/ -> http://" + handshake.getMappingIp() + ":" + handshake.getPort() + "/");
                        failMessages.add("https://101.35.221.134:" + config.getHttpsPort() + "/" + handshake.getMappingName() + "/ -> https://" + handshake.getMappingIp() + ":" + handshake.getPort() + "/");
                    }
                    else
                    {
                        failMessages.add("http://101.35.221.134:" + config.getHttpPort() + "/" + handshake.getMappingName() + "/  -> http://" + handshake.getMappingIp() + ":" + handshake.getPort() + "/" + handshake.getMappingName() + "/");
                        failMessages.add("https://101.35.221.134:" + config.getHttpsPort() + "/" + handshake.getMappingName() + "/ -> https://" + handshake.getMappingIp() + ":" + handshake.getPort() + "/" + handshake.getMappingName() + "/");
                    }

                }
            }
            channelHandlerContext.writeAndFlush(handshakeResult);
            return true;
        }
        return false;
    }
}
