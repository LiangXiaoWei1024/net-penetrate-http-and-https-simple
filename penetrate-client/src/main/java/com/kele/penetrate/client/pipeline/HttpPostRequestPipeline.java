package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.protocol.HttpPostRequest;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.HttpUtils;

@Register
@Recognizer
@SuppressWarnings("unused")
public class HttpPostRequestPipeline implements Func<Object, Boolean>
{
    @Autowired
    private ConnectHandler connectHandler;
    @Autowired
    private HttpUtils httpUtils;

    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof HttpPostRequest)
        {
            HttpPostRequest httpPostRequest = (HttpPostRequest) msg;

            httpUtils.post(httpPostRequest.getRequestUrl(), httpPostRequest.getHeaders(), requestResult ->
            {
                requestResult.setRequestId(httpPostRequest.getRequestId());
                connectHandler.send(requestResult);
            });
            return true;
        }
        return false;
    }
}
