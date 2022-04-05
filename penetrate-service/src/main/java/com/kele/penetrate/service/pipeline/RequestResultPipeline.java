package com.kele.penetrate.service.pipeline;

import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.Recognizer;
import com.kele.penetrate.factory.Register;
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
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(requestResult.getData()));
            if (requestResult.getHeaders() != null)
            {
                requestResult.getHeaders().forEach((k, v) ->
                        response.headers().set(k, v));
            }
            ConnectManager.MsgManager recordMsg = connectManager.getRecordMsg(requestResult.getRequestId());
            if(recordMsg != null && recordMsg.getChannelHandlerContext() != null){
                recordMsg.getChannelHandlerContext().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
            return true;
        }
        return false;
    }
}
