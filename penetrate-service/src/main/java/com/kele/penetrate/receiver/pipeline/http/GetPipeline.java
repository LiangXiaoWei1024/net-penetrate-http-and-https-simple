package com.kele.penetrate.receiver.pipeline.http;

import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.utils.http.AnalysisHttpRequest;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.PageTemplate;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Register
@SuppressWarnings("unused")
@Slf4j
public class GetPipeline implements Func<PipelineTransmission, Boolean>
{

    @Override
    public Boolean func(PipelineTransmission pipelineTransmission)
    {
        FullHttpRequest fullHttpRequest = pipelineTransmission.getFullHttpRequest();
        ChannelHandlerContext channelHandlerContext = pipelineTransmission.getChannelHandlerContext();

        if (AnalysisHttpRequest.getRequestType(fullHttpRequest) == RequestType.GET)
        {
            HttpHeaders headers = fullHttpRequest.headers();
            String contentType = headers.get("Content-Type");
            if (contentType != null)
            {
                log.error("get 不支持携带请求体");
                channelHandlerContext.writeAndFlush(PageTemplate.getAccessDeniedTemplate()).addListener(ChannelFutureListener.CLOSE);
            }
            else
            {
                Map<String, Object> requestHeaders = AnalysisHttpRequest.getRequestHeaders(fullHttpRequest);
                String homeUser = AnalysisHttpRequest.getHomeUser(fullHttpRequest);
                if(homeUser == null){
                    channelHandlerContext.writeAndFlush(PageTemplate.getNotFoundTemplate()).addListener(ChannelFutureListener.CLOSE);
                }
                String requestUrl = AnalysisHttpRequest.getRequestUrl(fullHttpRequest);
                //转发到客户端

            }

            return true;
        }
        return false;
    }
}
