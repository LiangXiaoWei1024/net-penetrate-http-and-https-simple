package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.enumeration.HypertextTransferProtocolType;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.protocol.HttpPostRequestText;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.HttpUtils;

@Register
@Recognizer
@SuppressWarnings("unused")
public class HttpPostRequestTextPipeline implements Func<Object, Boolean>
{

    @Autowired
    private ConnectHandler connectHandler;
    @Autowired
    private HttpUtils httpUtils;


    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof HttpPostRequestText)
        {
            HttpPostRequestText httpPostRequestText = (HttpPostRequestText) msg;
            httpUtils.postText(httpPostRequestText.getRequestUrl(), httpPostRequestText.getHeaders(),httpPostRequestText.getDataText(), requestResult ->
            {
                requestResult.setRequestId(httpPostRequestText.getRequestId());
                connectHandler.send(requestResult);
            });

            return true;
        }
        return false;
    }
}
