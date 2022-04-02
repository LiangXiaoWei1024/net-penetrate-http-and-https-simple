package com.kele.penetrate.receiver.pipeline.http;

import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.utils.http.AnalysisHttpsRequest;
import com.kele.penetrate.utils.Func;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Map;

@Register
@SuppressWarnings("unused")
public class PostPipeline implements Func<PipelineTransmission, Boolean>
{

    @Override
    public Boolean func(PipelineTransmission pipelineTransmission)
    {
        FullHttpRequest fullHttpRequest = pipelineTransmission.getFullHttpRequest();
        ChannelHandlerContext channelHandlerContext = pipelineTransmission.getChannelHandlerContext();
        if (AnalysisHttpsRequest.getRequestType(fullHttpRequest) == RequestType.POST)
        {
            System.out.println("进入http post");
            Map<String, Object> paramsFromChannel = AnalysisHttpsRequest.getParamsFromChannel(fullHttpRequest);
            Map<String, Object> requestHeaders = AnalysisHttpsRequest.getRequestHeaders(fullHttpRequest);
            String homeUser = AnalysisHttpsRequest.getHomeUser(fullHttpRequest);

            return true;
        }
        return false;
    }
}
