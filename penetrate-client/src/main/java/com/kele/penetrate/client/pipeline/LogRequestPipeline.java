package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.page.ClientLogPageManager;
import com.kele.penetrate.protocol.BaseRequest;
import com.kele.penetrate.utils.Func;

@Register(99)
@Recognizer
@SuppressWarnings("unused")
public class LogRequestPipeline implements Func<Object, Boolean>
{

    @Autowired
    private ClientLogPageManager clientLogPageManager;

    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof BaseRequest)
        {
            BaseRequest baseRequest = (BaseRequest) msg;
            clientLogPageManager.addLog("收到" + baseRequest.getRequestType().code + "请求 :  " + baseRequest.getRequestUrl());
        }
        return false;
    }
}
