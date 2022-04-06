package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.enumeration.HypertextTransferProtocolType;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.protocol.HttpPostRequestMultipart;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.HttpUtils;

@Register
@Recognizer
@SuppressWarnings("unused")
public class HttpPostRequestMultipartPipeline implements Func<Object, Boolean>
{
    @Autowired
    private ConnectHandler connectHandler;
    @Autowired
    private HttpUtils httpUtils;


    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof HttpPostRequestMultipart)
        {
            HttpPostRequestMultipart httpPostRequestMultipart = (HttpPostRequestMultipart) msg;

            httpUtils.postMultipart(httpPostRequestMultipart.getRequestUrl(), httpPostRequestMultipart.getHeaders(), httpPostRequestMultipart.getBodyMap(), httpPostRequestMultipart.getBodyFile(), requestResult ->
            {
                requestResult.setRequestId(httpPostRequestMultipart.getRequestId());
                connectHandler.send(requestResult);
            });

            return true;
        }
        return false;
    }
}
