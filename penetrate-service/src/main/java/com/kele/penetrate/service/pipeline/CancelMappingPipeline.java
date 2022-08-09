package com.kele.penetrate.service.pipeline;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.ServicePipeline;
import com.kele.penetrate.protocol.Cancel;
import com.kele.penetrate.protocol.CancelMappingResult;
import com.kele.penetrate.service.ConnectManager;
import com.kele.penetrate.utils.Func;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Register
@Recognizer
public class CancelMappingPipeline implements Func<ServicePipeline, Boolean>
{
    @Autowired
    private ConnectManager connectManager;

    @Override
    public Boolean func(ServicePipeline servicePipeline)
    {
        Object msg = servicePipeline.getMsg();
        ChannelHandlerContext channelHandlerContext = servicePipeline.getChannelHandlerContext();
        if (msg instanceof Cancel)
        {
            connectManager.get(channelHandlerContext.channel().id()).setCustomDomainName(null);
            channelHandlerContext.writeAndFlush(new CancelMappingResult());
            return true;
        }
        return false;
    }
}
