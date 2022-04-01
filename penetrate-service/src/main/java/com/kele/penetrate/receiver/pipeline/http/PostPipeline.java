package com.kele.penetrate.receiver.pipeline.http;

import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.receiver.http.AnalysisHttpRequest;
import com.kele.penetrate.utils.Func;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

@Register
@SuppressWarnings("unused")
public class PostPipeline implements Func<PipelineTransmission, Boolean>
{

    @Override
    public Boolean func(PipelineTransmission pipelineTransmission)
    {
        FullHttpRequest fullHttpRequest = pipelineTransmission.getFullHttpRequest();
        ChannelHandlerContext channelHandlerContext = pipelineTransmission.getChannelHandlerContext();
        if (AnalysisHttpRequest.getRequestType(fullHttpRequest) == RequestType.POST)
        {
            System.out.println("进如http post");
            return true;
        }
        return false;
    }
}
