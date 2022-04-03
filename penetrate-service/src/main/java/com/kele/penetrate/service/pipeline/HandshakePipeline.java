package com.kele.penetrate.service.pipeline;

import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.Recognizer;
import com.kele.penetrate.factory.Register;
import com.kele.penetrate.protocol.Handshake;
import com.kele.penetrate.service.ConnectManager;
import com.kele.penetrate.utils.Func;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Register
@Recognizer
public class HandshakePipeline implements Func<Object, Boolean>
{
    @Autowired
    private ConnectManager connectManager;

    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof Handshake)
        {
            Handshake handshake = (Handshake) msg;
            log.info("新的服务端进来" + handshake);
            System.out.println(connectManager == null);
            return true;
        }
        return false;
    }
}
