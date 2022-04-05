package com.kele.penetrate.receiver.pipeline.https;

import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.utils.Func;

@Register
@SuppressWarnings("unused")
public class GetPipeline implements Func<PipelineTransmission, Boolean>
{
    @Override
    public Boolean func(PipelineTransmission pipelineTransmission)
    {
        System.out.println("https GET");
        return false;
    }
}
