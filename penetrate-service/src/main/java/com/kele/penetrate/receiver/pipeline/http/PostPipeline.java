package com.kele.penetrate.receiver.pipeline.http;

import com.kele.penetrate.enumeration.RequestContentType;
import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.utils.http.AnalysisHttpPostRequest;
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
        if (AnalysisHttpPostRequest.getRequestType(fullHttpRequest) == RequestType.POST)
        {
            System.out.println("进入http post");
            Map<String, String> requestHeaders = AnalysisHttpPostRequest.getRequestHeaders(fullHttpRequest);
            String homeUser = AnalysisHttpPostRequest.getHomeUser(fullHttpRequest);
            String contentType = fullHttpRequest.headers().get("Content-Type");
            System.out.println(contentType);
            //<editor-fold desc="处理 x-www-form-urlencoded">
            if (contentType.contains(RequestContentType.X_WWW_FORM_URLENCODED.getCode()))
            {
                Map<String, String> formBody = AnalysisHttpPostRequest.getFormBody(fullHttpRequest);
                System.out.println(formBody);
            }
            //</editor-fold>

            //<editor-fold desc="处理 multipart/form-data">
            else if (contentType.contains(RequestContentType.MULTIPART_FORM_DATA.getCode()))
            {
                Map<String, String> multipartBody = AnalysisHttpPostRequest.getMultipartBody(fullHttpRequest);

            }
            //</editor-fold>

            //<editor-fold desc="处理 application(json xml javaScript),text(plain html)">
            else if (contentType.contains(RequestContentType.APPLICATION_JSON.getCode()) ||
                    contentType.contains(RequestContentType.APPLICATION_JAVASCRIPT.getCode()) ||
                    contentType.contains(RequestContentType.APPLICATION_XML.getCode()) ||
                    contentType.contains(RequestContentType.TEXT_PLAIN.getCode())
            )
            {
                String textBody = AnalysisHttpPostRequest.getTextBody(fullHttpRequest);
            }
            //</editor-fold>

            else
            {
                //处理不了的请求方式
            }
            return true;
        }
        return false;
    }
}
