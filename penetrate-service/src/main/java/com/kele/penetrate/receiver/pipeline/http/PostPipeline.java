package com.kele.penetrate.receiver.pipeline.http;

import com.kele.penetrate.enumeration.RequestContentType;
import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.protocol.HttpPostRequestForm;
import com.kele.penetrate.protocol.HttpPostRequestMultipart;
import com.kele.penetrate.protocol.HttpPostRequestText;
import com.kele.penetrate.protocol.RequestFile;
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

import java.util.List;
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
        if (AnalysisHttpPostRequest.getRequestType(fullHttpRequest) == RequestType.POST)
        {
            System.out.println("进入http post");
            Map<String, String> requestHeaders = AnalysisHttpPostRequest.getRequestHeaders(fullHttpRequest);
            String mappingName = AnalysisHttpPostRequest.getHomeUser(fullHttpRequest);
            String contentType = fullHttpRequest.headers().get("Content-Type");
            ConnectHandler connectHandler = connectManager.get(mappingName);
            String requestUrl = AnalysisHttpGetRequest.getRequestUrl(fullHttpRequest);
            requestUrl = "http://" + connectHandler.getMappingIp() + ":" + connectHandler.getPort() + requestUrl;
            if (connectHandler != null)
            {
                //<editor-fold desc="处理 x-www-form-urlencoded">
                if (contentType.contains(RequestContentType.X_WWW_FORM_URLENCODED.getCode()))
                {
                    HttpPostRequestForm httpPostRequestForm = new HttpPostRequestForm();
                    httpPostRequestForm.setRequestId(uuidUtils.getUUID());
                    httpPostRequestForm.setHeaders(requestHeaders);
                    httpPostRequestForm.setDataBody(AnalysisHttpPostRequest.getFormBody(fullHttpRequest));
                    httpPostRequestForm.setRequestUrl(requestUrl);

                    connectManager.recordMsg(httpPostRequestForm, channelHandlerContext);
                    connectHandler.reply(httpPostRequestForm);
                }
                //</editor-fold>

                //<editor-fold desc="处理 multipart/form-data">
                else if (contentType.contains(RequestContentType.MULTIPART_FORM_DATA.getCode()))
                {
                    HttpPostRequestMultipart httpPostRequestMultipart = new HttpPostRequestMultipart();
                    httpPostRequestMultipart.setRequestId(uuidUtils.getUUID());
                    httpPostRequestMultipart.setRequestUrl(requestUrl);
                    httpPostRequestMultipart.setHeaders(requestHeaders);
                    httpPostRequestMultipart.setBodyMap(AnalysisHttpPostRequest.getMultipartBodyAttribute(fullHttpRequest));
                    httpPostRequestMultipart.setBodyFile(AnalysisHttpPostRequest.getMultipartBodyFiles(fullHttpRequest));

                    connectManager.recordMsg(httpPostRequestMultipart, channelHandlerContext);
                    connectHandler.reply(httpPostRequestMultipart);
                }
                //</editor-fold>

                //<editor-fold desc="处理 application(json xml javaScript),text(plain html)">
                else if (contentType.contains(RequestContentType.APPLICATION_JSON.getCode()) ||
                        contentType.contains(RequestContentType.APPLICATION_JAVASCRIPT.getCode()) ||
                        contentType.contains(RequestContentType.APPLICATION_XML.getCode()) ||
                        contentType.contains(RequestContentType.TEXT_PLAIN.getCode())
                )
                {
                    HttpPostRequestText httpPostRequestText = new HttpPostRequestText();
                    httpPostRequestText.setRequestId(uuidUtils.getUUID());
                    httpPostRequestText.setRequestUrl(requestUrl);
                    httpPostRequestText.setHeaders(requestHeaders);
                    httpPostRequestText.setDataText(AnalysisHttpPostRequest.getTextBody(fullHttpRequest));
                    connectManager.recordMsg(httpPostRequestText, channelHandlerContext);
                    connectHandler.reply(httpPostRequestText);
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
