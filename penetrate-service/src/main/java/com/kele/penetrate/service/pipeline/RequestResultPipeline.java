package com.kele.penetrate.service.pipeline;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.ServicePipeline;
import com.kele.penetrate.protocol.RequestResult;
import com.kele.penetrate.service.ConnectManager;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.PageTemplate;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;


@SuppressWarnings("unused")
@Slf4j
@Register
@Recognizer
public class RequestResultPipeline implements Func<ServicePipeline, Boolean>
{
    @Autowired
    private ConnectManager connectManager;
    @Autowired
    private PageTemplate pageTemplate;

    @Override
    public Boolean func(ServicePipeline servicePipeline)
    {
        Object msg = servicePipeline.getMsg();
        ChannelHandlerContext channelHandlerContext = servicePipeline.getChannelHandlerContext();
        if (msg instanceof RequestResult)
        {
            RequestResult requestResult = (RequestResult) msg;
            if (requestResult.isSuccess())
            {
                FullHttpResponse responseSuccess;

                if (requestResult.getData() != null)
                {
                    responseSuccess = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, new HttpResponseStatus(requestResult.getCode(), ""), Unpooled.copiedBuffer(requestResult.getData()));
                }
                else
                {
                    responseSuccess = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, new HttpResponseStatus(requestResult.getCode(), ""));
                }

                if (requestResult.getHeaders() != null)
                {
                    requestResult.getHeaders().forEach((k, v) ->
                            responseSuccess.headers().set(k, v));
                }
                ConnectManager.MsgManager recordMsg = connectManager.getRecordMsg(requestResult.getRequestId());
                if (recordMsg != null && recordMsg.getChannelHandlerContext() != null)
                {
                    recordMsg.getChannelHandlerContext().writeAndFlush(responseSuccess).addListener(ChannelFutureListener.CLOSE);
                }
            }
            else
            {
                ConnectManager.MsgManager recordMsg = connectManager.getRecordMsg(requestResult.getRequestId());

                if (recordMsg != null && recordMsg.getChannelHandlerContext() != null)
                {
                    FullHttpResponse responseFail = pageTemplate.createTemplate(requestResult.getFailMessage(), requestResult.getFailMessage(), new HttpResponseStatus(requestResult.getCode(), requestResult.getFailMessage()));
                    recordMsg.getChannelHandlerContext().writeAndFlush(responseFail).addListener(ChannelFutureListener.CLOSE);
                }
            }
            return true;
        }
        return false;
    }
}
