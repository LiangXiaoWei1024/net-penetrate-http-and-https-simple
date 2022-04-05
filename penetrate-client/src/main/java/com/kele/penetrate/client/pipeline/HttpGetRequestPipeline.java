package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.protocol.HttpGetRequest;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.HttpUtils;

@Register
@Recognizer
@SuppressWarnings("unused")
public class HttpGetRequestPipeline implements Func<Object, Boolean>
{

    @Autowired
    private ConnectHandler connectHandler;
    @Autowired
    private HttpUtils httpUtils;


    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof HttpGetRequest)
        {
            HttpGetRequest httpGetRequest = (HttpGetRequest) msg;
            httpUtils.get(httpGetRequest.getRequestUrl(), httpGetRequest.getHeaders(), requestResult ->
            {
                requestResult.setRequestId(httpGetRequest.getRequestId());
                connectHandler.send(requestResult);
            });
            return true;
        }
        return false;
    }
}
