package com.kele.penetrate.service.pipeline;

import com.kele.penetrate.config.Config;
import com.kele.penetrate.enumeration.HypertextTransferProtocolType;
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
            List<String> messages = new ArrayList<>();
            handshakeResult.setFailMessages(messages);
            boolean success;

            if (!versionInfo.getVersion().equals(handshake.getVersion()))
            {
                for (int i = 0; i < versionInfo.getContents().size(); i++)
                {
                    messages.add(versionInfo.getContents().getString(i));
                }
            }
            else
            {
                if (connectManager.isExist(handshake.getCustomDomainName()))
                {
                    //映射名称已经存在
                    channelHandlerContext.writeAndFlush(new HandshakeResult());
                    messages.add("域名已被别人使用 [" + handshake.getCustomDomainName() + "]");
                }
                else
                {
                    ConnectHandler connectHandler = connectManager.get(channelHandlerContext.channel().id());
                    connectHandler.setCustomDomainName(handshake.getCustomDomainName());
                    messages.add("访问域名: " + HypertextTransferProtocolType.HTTP.code + "://" + handshake.getCustomDomainName() + ":" + config.getHttpPort());
                    messages.add("访问域名: " + HypertextTransferProtocolType.HTTPS.code + "://" + handshake.getCustomDomainName() + ":" + config.getHttpsPort());
                    handshakeResult.setSuccess(true);
                }
            }
            channelHandlerContext.writeAndFlush(handshakeResult);
            return true;
        }
        return false;
    }
}
