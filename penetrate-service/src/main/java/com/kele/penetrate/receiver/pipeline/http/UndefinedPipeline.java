package com.kele.penetrate.receiver.pipeline.http;

import com.kele.penetrate.factory.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.utils.Func;

@Register(-1)
@SuppressWarnings("unused")
public class UndefinedPipeline implements Func<PipelineTransmission, Boolean>
{
    @Override
    public Boolean func(PipelineTransmission pipelineTransmission)
    {
        System.out.println("未定义");
        return false;
    }
}
