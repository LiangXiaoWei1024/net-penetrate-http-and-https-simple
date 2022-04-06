package com.kele.penetrate.receiver.pipeline;

import com.kele.penetrate.enumeration.HypertextTransferProtocolType;
import com.kele.penetrate.enumeration.RequestContentType;
import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.MultipartBody;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.protocol.PostRequest;
import com.kele.penetrate.protocol.PostRequestForm;
import com.kele.penetrate.protocol.PostRequestMultipart;
import com.kele.penetrate.protocol.PostRequestText;
import com.kele.penetrate.service.ConnectHandler;
import com.kele.penetrate.service.ConnectManager;
import com.kele.penetrate.utils.PageTemplate;
import com.kele.penetrate.utils.UUIDUtils;
import com.kele.penetrate.utils.http.AnalysisHttpGetRequest;
import com.kele.penetrate.utils.http.AnalysisHttpPostRequest;
import com.kele.penetrate.utils.Func;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Register
@SuppressWarnings("unused")
@Slf4j
@Recognizer
public class PostPipeline implements Func<PipelineTransmission, Boolean>
{
    @Autowired
    private UUIDUtils uuidUtils;
    @Autowired
    private ConnectManager connectManager;

    @Override
    public Boolean func(PipelineTransmission pipelineTransmission)
    {
        FullHttpRequest fullHttpRequest = pipelineTransmission.getFullHttpRequest();
        ChannelHandlerContext channelHandlerContext = pipelineTransmission.getChannelHandlerContext();
        HypertextTransferProtocolType hypertextTransferProtocolType = pipelineTransmission.getHypertextTransferProtocolType();

        if (AnalysisHttpPostRequest.getRequestType(fullHttpRequest) == RequestType.POST)
        {
            Map<String, String> requestHeaders = AnalysisHttpPostRequest.getRequestHeaders(fullHttpRequest);
            String mappingName = AnalysisHttpPostRequest.getHomeUser(fullHttpRequest);
            String contentType = fullHttpRequest.headers().get("Content-Type");
            ConnectHandler connectHandler = connectManager.get(mappingName);

            if (connectHandler != null)
            {
                String requestUrl = AnalysisHttpGetRequest.getRequestUrl(fullHttpRequest, connectHandler.isFilterMappingName());
                requestUrl = hypertextTransferProtocolType.getCode() + "://" + connectHandler.getMappingIp() + ":" + connectHandler.getPort() + requestUrl;
                if (contentType == null)
                {
                    PostRequest postRequest = new PostRequest();
                    postRequest.setRequestId(uuidUtils.getUUID());
                    postRequest.setRequestUrl(requestUrl);
                    postRequest.setHeaders(requestHeaders);

                    connectManager.recordMsg(postRequest, channelHandlerContext);
                    connectHandler.reply(postRequest);
                }
                else
                {

                    //<editor-fold desc="处理 x-www-form-urlencoded">
                    if (contentType.contains(RequestContentType.X_WWW_FORM_URLENCODED.getCode()))
                    {
                        PostRequestForm postRequestForm = new PostRequestForm();
                        postRequestForm.setRequestId(uuidUtils.getUUID());
                        postRequestForm.setHeaders(requestHeaders);
                        postRequestForm.setDataBody(AnalysisHttpPostRequest.getFormBody(fullHttpRequest));
                        postRequestForm.setRequestUrl(requestUrl);

                        connectManager.recordMsg(postRequestForm, channelHandlerContext);
                        connectHandler.reply(postRequestForm);
                    }
                    //</editor-fold>

                    //<editor-fold desc="处理 multipart/form-data">
                    else if (contentType.contains(RequestContentType.MULTIPART_FORM_DATA.getCode()))
                    {
                        PostRequestMultipart postRequestMultipart = new PostRequestMultipart();
                        MultipartBody multipartBody = AnalysisHttpPostRequest.getMultipartBody(fullHttpRequest);
                        postRequestMultipart.setRequestId(uuidUtils.getUUID());
                        postRequestMultipart.setRequestUrl(requestUrl);
                        postRequestMultipart.setHeaders(requestHeaders);
                        postRequestMultipart.setBodyMap(multipartBody.getBodyMap());
                        postRequestMultipart.setBodyFile(multipartBody.getBodyFiles());

                        connectManager.recordMsg(postRequestMultipart, channelHandlerContext);
                        connectHandler.reply(postRequestMultipart);
                    }
                    //</editor-fold>

                    //<editor-fold desc="处理 application(json xml javaScript),text(plain html)">
                    else if (contentType.contains(RequestContentType.APPLICATION_JSON.getCode()) ||
                            contentType.contains(RequestContentType.APPLICATION_JAVASCRIPT.getCode()) ||
                            contentType.contains(RequestContentType.APPLICATION_XML.getCode()) ||
                            contentType.contains(RequestContentType.TEXT_PLAIN.getCode())
                    )
                    {
                        PostRequestText postRequestText = new PostRequestText();
                        postRequestText.setRequestId(uuidUtils.getUUID());
                        postRequestText.setRequestUrl(requestUrl);
                        postRequestText.setHeaders(requestHeaders);
                        postRequestText.setDataText(AnalysisHttpPostRequest.getTextBody(fullHttpRequest));
                        connectManager.recordMsg(postRequestText, channelHandlerContext);
                        connectHandler.reply(postRequestText);
                    }
                    //</editor-fold>

                    //<editor-fold desc="无法处理的 Content-Type">
                    else
                    {
                        log.error("无法处理的Content-Type：" + contentType);
                        channelHandlerContext.writeAndFlush(PageTemplate.getUnableProcess()).addListener(ChannelFutureListener.CLOSE);
                    }
                    //</editor-fold>
                }
            }
            else
            {
                FullHttpResponse serviceUnavailableTemplate = PageTemplate.getNotFoundTemplate();
                channelHandlerContext.writeAndFlush(serviceUnavailableTemplate).addListener(ChannelFutureListener.CLOSE);
            }
            return true;
        }
        return false;
    }
}
