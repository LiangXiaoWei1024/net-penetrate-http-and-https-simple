package com.kele.penetrate.service.pipeline;

import com.kele.penetrate.enumeration.ResponseContentType;
import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.Recognizer;
import com.kele.penetrate.factory.Register;
import com.kele.penetrate.pojo.ServicePipeline;
import com.kele.penetrate.protocol.Handshake;
import com.kele.penetrate.protocol.HandshakeResult;
import com.kele.penetrate.protocol.HttpGetRequestResult;
import com.kele.penetrate.service.ConnectHandler;
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

import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
@Slf4j
@Register
@Recognizer
public class HttpGetRequestResultPipeline implements Func<ServicePipeline, Boolean>
{
    @Autowired
    private ConnectManager connectManager;

    @Override
    public Boolean func(ServicePipeline servicePipeline)
    {
        Object msg = servicePipeline.getMsg();
        ChannelHandlerContext channelHandlerContext = servicePipeline.getChannelHandlerContext();
        if (msg instanceof HttpGetRequestResult)
        {
            System.out.println("收到");
            HttpGetRequestResult httpGetRequestResult = (HttpGetRequestResult) msg;
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(httpGetRequestResult.getData()));
            if (httpGetRequestResult.getHeaders() != null)
            {
                httpGetRequestResult.getHeaders().forEach((k, v) ->
                        response.headers().set(k, v));
            }
            ConnectManager.MsgManager recordMsg = connectManager.getRecordMsg(httpGetRequestResult.getRequestId());
            if(recordMsg != null && recordMsg.getChannelHandlerContext() != null){
                recordMsg.getChannelHandlerContext().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
            return true;
        }
        return false;
    }
}
