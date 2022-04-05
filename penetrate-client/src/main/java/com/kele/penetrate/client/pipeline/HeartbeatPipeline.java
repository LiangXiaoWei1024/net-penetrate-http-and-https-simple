package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.protocol.Heartbeat;
import com.kele.penetrate.utils.Func;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@Recognizer
@Register
public class HeartbeatPipeline implements Func<Object, Boolean>
{
    @Autowired
    private ConnectHandler connectHandler;

    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof Heartbeat)
        {
            connectHandler.send(msg);
            return true;
        }
        return false;
    }
}
