package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.protocol.HandshakeResult;
import com.kele.penetrate.utils.Func;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@Register
public class HandshakeResultPipeline implements Func<Object, Boolean>
{
    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof HandshakeResult)
        {
            HandshakeResult handshakeResult = (HandshakeResult) msg;
            if (handshakeResult.isSuccess())
            {
                log.info("映射成功，访问地址：" + handshakeResult.getAccessAddress());
            }
            else
            {
                log.info("映射失败，名称已经被占用，请更换一个映射名称");
            }
            return true;
        }
        return false;
    }
}
