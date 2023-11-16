package com.kele.penetrate.receiver.pipeline;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.PageTemplate;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

@Register(-1)
@SuppressWarnings("unused")
@Recognizer
public class UndefinedPipeline implements Func<PipelineTransmission, Boolean>
{
    @Autowired
    private PageTemplate pageTemplate;

    @Override
    public Boolean func(PipelineTransmission pipelineTransmission)
    {
        FullHttpResponse serviceUnavailableTemplate = pageTemplate.get_ServiceUnavailable_Template();
        pipelineTransmission.getChannelHandlerContext().writeAndFlush(serviceUnavailableTemplate).addListener(ChannelFutureListener.CLOSE);
        //释放资源
        serviceUnavailableTemplate.release();
        pipelineTransmission.getFullHttpRequest().release();
        return true;
    }
}
