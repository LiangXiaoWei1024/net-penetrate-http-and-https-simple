package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.protocol.GetRequest;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.HttpUtils;

@Register
@Recognizer
@SuppressWarnings("unused")
public class GetRequestPipeline implements Func<Object, Boolean>
{

    @Autowired
    private ConnectHandler connectHandler;
    @Autowired
    private HttpUtils httpUtils;

    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof GetRequest)
        {
            GetRequest getRequest = (GetRequest) msg;
            httpUtils.get(getRequest.getRequestUrl(), getRequest.getHeaders(), requestResult ->
            {
                requestResult.setRequestId(getRequest.getRequestId());
                connectHandler.send(requestResult);
            });
            return true;
        }
        return false;
    }
}
