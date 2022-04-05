package com.kele.penetrate.receiver.pipeline.http;

import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.PageTemplate;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpResponse;

@Register(-1)
@SuppressWarnings("unused")
public class UndefinedPipeline implements Func<PipelineTransmission, Boolean>
{
    @Override
    public Boolean func(PipelineTransmission pipelineTransmission)
    {
        FullHttpResponse serviceUnavailableTemplate = PageTemplate.getServiceUnavailableTemplate();
        pipelineTransmission.getChannelHandlerContext().writeAndFlush(serviceUnavailableTemplate).addListener(ChannelFutureListener.CLOSE);
        return true;
    }
}
