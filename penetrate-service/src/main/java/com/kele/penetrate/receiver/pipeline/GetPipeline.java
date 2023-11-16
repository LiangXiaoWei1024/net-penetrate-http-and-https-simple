package com.kele.penetrate.receiver.pipeline;

import com.kele.penetrate.enumeration.HypertextTransferProtocolType;
import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.protocol.RequestNotBody;
import com.kele.penetrate.service.ConnectHandler;
import com.kele.penetrate.service.ConnectManager;
import com.kele.penetrate.utils.UUIDUtils;
import com.kele.penetrate.utils.http.AnalysisHttpGetRequest;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.PageTemplate;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * GET请求管道，不GET请求不支持携带请求体
 */
@Slf4j
@Register
@Recognizer
@SuppressWarnings("unused")
public class GetPipeline implements Func<PipelineTransmission, Boolean>
{

    @Autowired
    private ConnectManager connectManager;
    @Autowired
    private UUIDUtils uuidUtils;
    @Autowired
    private PageTemplate pageTemplate;
    @Autowired
    private AnalysisHttpGetRequest analysisHttpGetRequest;

    @Override
    public Boolean func(PipelineTransmission pipelineTransmission)
    {
        FullHttpRequest fullHttpRequest = pipelineTransmission.getFullHttpRequest();
        ChannelHandlerContext channelHandlerContext = pipelineTransmission.getChannelHandlerContext();
        HypertextTransferProtocolType hypertextTransferProtocolType = pipelineTransmission.getHypertextTransferProtocolType();

        if (analysisHttpGetRequest.getRequestType(fullHttpRequest) == RequestType.GET)
        {
            HttpHeaders headers = fullHttpRequest.headers();
            String contentType = headers.get("Content-Type");
            FullHttpResponse serviceTemplate = null;
            if (contentType != null)
            {
                log.error("get 不支持携带请求体");
                serviceTemplate = pageTemplate.get_GetBodyAccessDenied_Template();
                channelHandlerContext.writeAndFlush(serviceTemplate).addListener(ChannelFutureListener.CLOSE);
            }
            else
            {
                Map<String, String> requestHeaders = analysisHttpGetRequest.getRequestHeaders(fullHttpRequest,channelHandlerContext);
                String host = analysisHttpGetRequest.getHost(fullHttpRequest);
                if (host == null || !connectManager.isExist(host))
                {
                    serviceTemplate = pageTemplate.get_NotFound_Template();
                    channelHandlerContext.writeAndFlush(serviceTemplate).addListener(ChannelFutureListener.CLOSE);
                }
                else
                {
                    ConnectHandler connectHandler = connectManager.get(host);
                    if (connectHandler != null)
                    {
                        RequestNotBody requestNotBody = new RequestNotBody();
                        requestNotBody.setRequestId(uuidUtils.getUUID());
                        requestNotBody.setRequestProtocolType(hypertextTransferProtocolType);
                        requestNotBody.setRequestUri(analysisHttpGetRequest.getRequestUrl(fullHttpRequest));
                        requestNotBody.setHeaders(requestHeaders);
                        requestNotBody.setRequestType(RequestType.GET);

                        connectManager.addRecordMessage(requestNotBody, channelHandlerContext);
                        connectHandler.reply(requestNotBody);
                    }
                    else
                    {
                        serviceTemplate = pageTemplate.get_NotFound_Template();
                        channelHandlerContext.writeAndFlush(serviceTemplate).addListener(ChannelFutureListener.CLOSE);
                    }
                }
            }
            //释放资源
            if(serviceTemplate != null){
                serviceTemplate.release();
            }
            pipelineTransmission.getFullHttpRequest().release();
            return true;
        }
        return false;
    }
}
