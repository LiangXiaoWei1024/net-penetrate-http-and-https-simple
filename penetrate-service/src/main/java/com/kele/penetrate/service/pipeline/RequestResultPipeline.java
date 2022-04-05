package com.kele.penetrate.service.pipeline;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.ServicePipeline;
import com.kele.penetrate.protocol.RequestResult;
import com.kele.penetrate.service.ConnectManager;
import com.kele.penetrate.utils.Func;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;


@SuppressWarnings("unused")
@Slf4j
@Register
@Recognizer
public class RequestResultPipeline implements Func<ServicePipeline, Boolean>
{
    @Autowired
    private ConnectManager connectManager;

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
                FullHttpResponse responseSuccess = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(requestResult.getData()));
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
                FullHttpResponse responseFail = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(requestResult.getFailMessage().getBytes(StandardCharsets.UTF_8)));
                ConnectManager.MsgManager recordMsg = connectManager.getRecordMsg(requestResult.getRequestId());
                if (requestResult.getFailMessage().contains("request timeout"))
                {
                    responseFail.setStatus(HttpResponseStatus.REQUEST_TIMEOUT);
                }
                if (requestResult.getFailMessage().contains("connect timeout"))
                {
                    responseFail.setStatus(HttpResponseStatus.GATEWAY_TIMEOUT);
                }

                if (recordMsg != null && recordMsg.getChannelHandlerContext() != null)
                {
                    recordMsg.getChannelHandlerContext().writeAndFlush(responseFail).addListener(ChannelFutureListener.CLOSE);
                }
            }
            return true;
        }
        return false;
    }
}
