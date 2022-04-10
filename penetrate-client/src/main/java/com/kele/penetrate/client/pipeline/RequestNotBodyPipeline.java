package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.protocol.RequestNotBody;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.HttpUtils;

@Register
@Recognizer
@SuppressWarnings("unused")
public class RequestNotBodyPipeline implements Func<Object, Boolean>
{

    @Autowired
    private ConnectHandler connectHandler;
    @Autowired
    private HttpUtils httpUtils;

    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof RequestNotBody)
        {
            RequestNotBody requestNotBody = (RequestNotBody) msg;
            httpUtils.requestNotBody(requestNotBody.getRequestType(), requestNotBody.getRequestUrl(), requestNotBody.getHeaders(), requestResult ->
            {
                requestResult.setRequestId(requestNotBody.getRequestId());
                connectHandler.send(requestResult);
            });
            return true;
        }
        return false;
    }
}
