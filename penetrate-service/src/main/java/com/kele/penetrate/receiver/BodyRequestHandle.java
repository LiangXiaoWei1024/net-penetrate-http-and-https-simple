package com.kele.penetrate.receiver;

import com.kele.penetrate.enumeration.HypertextTransferProtocolType;
import com.kele.penetrate.enumeration.RequestContentType;
import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.pojo.MultipartBody;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.protocol.RequestFormBody;
import com.kele.penetrate.protocol.RequestMultipartBody;
import com.kele.penetrate.protocol.RequestNotBody;
import com.kele.penetrate.protocol.RequestTextBody;
import com.kele.penetrate.service.ConnectHandler;
import com.kele.penetrate.service.ConnectManager;
import com.kele.penetrate.utils.PageTemplate;
import com.kele.penetrate.utils.UUIDUtils;
import com.kele.penetrate.utils.http.AnalysisHttpPostRequest;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Recognizer
@Slf4j
@SuppressWarnings("unused")
public class BodyRequestHandle
{
    @Autowired
    private UUIDUtils uuidUtils;
    @Autowired
    private ConnectManager connectManager;
    @Autowired
    private PageTemplate pageTemplate;
    @Autowired
    private AnalysisHttpPostRequest analysisHttpPostRequest;

    public boolean handle(PipelineTransmission pipelineTransmission)
    {
        FullHttpRequest fullHttpRequest = pipelineTransmission.getFullHttpRequest();
        ChannelHandlerContext channelHandlerContext = pipelineTransmission.getChannelHandlerContext();
        HypertextTransferProtocolType hypertextTransferProtocolType = pipelineTransmission.getHypertextTransferProtocolType();
        RequestType requestType = analysisHttpPostRequest.getRequestType(fullHttpRequest);

        if (requestType == RequestType.POST || requestType == RequestType.PUT || requestType == RequestType.PATCH || requestType == RequestType.DELETE)
        {
            Map<String, String> requestHeaders = analysisHttpPostRequest.getRequestHeaders(fullHttpRequest);
            String mappingName = analysisHttpPostRequest.getHomeUser(fullHttpRequest);
            String contentType = fullHttpRequest.headers().get("Content-Type");
            ConnectHandler connectHandler = this.connectManager.get(mappingName);

            if (connectHandler != null)
            {
                String requestUrl = analysisHttpPostRequest.getRequestUrl(fullHttpRequest, connectHandler.isFilterMappingName());
                requestUrl = hypertextTransferProtocolType.code + "://" + connectHandler.getMappingIp() + ":" + connectHandler.getPort() + requestUrl;
                if (contentType == null)
                {
                    RequestNotBody requestNotBody = new RequestNotBody();
                    requestNotBody.setRequestId(uuidUtils.getUUID());
                    requestNotBody.setRequestUrl(requestUrl);
                    requestNotBody.setHeaders(requestHeaders);
                    requestNotBody.setRequestType(requestType);
                    this.connectManager.recordMsg(requestNotBody, channelHandlerContext);
                    connectHandler.reply(requestNotBody);
                }
                else
                {
                    //<editor-fold desc="处理 x-www-form-urlencoded">
                    if (contentType.contains(RequestContentType.X_WWW_FORM_URLENCODED.code))
                    {
                        RequestFormBody requestFormBody = new RequestFormBody();
                        requestFormBody.setRequestType(requestType);
                        requestFormBody.setRequestId(uuidUtils.getUUID());
                        requestFormBody.setHeaders(requestHeaders);
                        requestFormBody.setDataBody(analysisHttpPostRequest.getFormBody(fullHttpRequest));
                        requestFormBody.setRequestUrl(requestUrl);

                        this.connectManager.recordMsg(requestFormBody, channelHandlerContext);
                        connectHandler.reply(requestFormBody);
                    }
                    //</editor-fold>

                    //<editor-fold desc="处理 multipart/form-data">
                    else if (contentType.contains(RequestContentType.MULTIPART_FORM_DATA.code))
                    {
                        RequestMultipartBody requestMultipartBody = new RequestMultipartBody();
                        requestMultipartBody.setRequestId(uuidUtils.getUUID());
                        requestMultipartBody.setRequestType(requestType);
                        requestMultipartBody.setRequestUrl(requestUrl);
                        requestMultipartBody.setHeaders(requestHeaders);
                        MultipartBody multipartBody = analysisHttpPostRequest.getMultipartBody(fullHttpRequest);
                        requestMultipartBody.setBodyMap(multipartBody.getBodyMap());
                        requestMultipartBody.setBodyFile(multipartBody.getBodyFiles());

                        this.connectManager.recordMsg(requestMultipartBody, channelHandlerContext);
                        connectHandler.reply(requestMultipartBody);
                    }
                    //</editor-fold>

                    //<editor-fold desc="处理 application(json xml javaScript),text(plain html)">
                    else if (contentType.contains(RequestContentType.APPLICATION_JSON.code) ||
                            contentType.contains(RequestContentType.APPLICATION_JAVASCRIPT.code) ||
                            contentType.contains(RequestContentType.APPLICATION_XML.code) ||
                            contentType.contains(RequestContentType.TEXT_PLAIN.code)
                    )
                    {
                        RequestTextBody requestTextBody = new RequestTextBody();
                        requestTextBody.setRequestId(uuidUtils.getUUID());
                        requestTextBody.setRequestType(requestType);
                        requestTextBody.setRequestUrl(requestUrl);
                        requestTextBody.setHeaders(requestHeaders);
                        requestTextBody.setDataText(analysisHttpPostRequest.getTextBody(fullHttpRequest));
                        this.connectManager.recordMsg(requestTextBody, channelHandlerContext);
                        connectHandler.reply(requestTextBody);
                    }
                    //</editor-fold>

                    //<editor-fold desc="无法处理的 Content-Type">
                    else
                    {
                        log.error("无法处理的Content-Type：" + contentType);
                        channelHandlerContext.writeAndFlush(pageTemplate.getUnableProcess()).addListener(ChannelFutureListener.CLOSE);
                    }
                    //</editor-fold>
                }
            }
            else
            {
                FullHttpResponse serviceUnavailableTemplate = pageTemplate.getNotFoundTemplate();
                channelHandlerContext.writeAndFlush(serviceUnavailableTemplate).addListener(ChannelFutureListener.CLOSE);
            }
            return true;
        }
        return false;
    }
}
