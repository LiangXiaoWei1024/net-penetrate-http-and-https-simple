package com.kele.penetrate.receiver.pipeline;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.receiver.BodyRequestHandle;
import com.kele.penetrate.utils.Func;
import lombok.extern.slf4j.Slf4j;


@Register
@SuppressWarnings("unused")
@Slf4j
@Recognizer
public class PutPipeline implements Func<PipelineTransmission, Boolean>
{
    @Autowired
    private BodyRequestHandle bodyRequestHandle;

    @Override
    public Boolean func(PipelineTransmission pipelineTransmission)
    {
        // POST | DELETE | PUT | PATCH 几个的处理方法一样，暂时不统一处理，防止以后变化方便分开处理
        return bodyRequestHandle.handle(pipelineTransmission);
    }
}
