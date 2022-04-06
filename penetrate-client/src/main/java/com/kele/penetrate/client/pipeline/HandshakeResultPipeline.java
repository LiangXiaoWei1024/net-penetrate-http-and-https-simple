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
                log.info("与服务器连接开启成功,访问地址: \r\n" + handshakeResult.getAccessAddress());
            }
            else
            {
                log.info("与服务器连接失败: \r\n" + handshakeResult.getFailMessage());
            }
            return true;
        }
        return false;
    }
}
