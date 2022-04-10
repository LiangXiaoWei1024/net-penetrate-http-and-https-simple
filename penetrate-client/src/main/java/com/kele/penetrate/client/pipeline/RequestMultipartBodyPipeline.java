package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.protocol.RequestMultipartBody;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.HttpUtils;

@Register
@Recognizer
@SuppressWarnings("unused")
public class RequestMultipartBodyPipeline implements Func<Object, Boolean>
{
    @Autowired
    private ConnectHandler connectHandler;
    @Autowired
    private HttpUtils httpUtils;


    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof RequestMultipartBody)
        {
            RequestMultipartBody requestMultipartBody = (RequestMultipartBody) msg;

            httpUtils.requestMultipartBody(requestMultipartBody.getRequestType(), requestMultipartBody.getRequestUrl(), requestMultipartBody.getHeaders(), requestMultipartBody.getBodyMap(), requestMultipartBody.getBodyFile(), requestResult ->
            {
                requestResult.setRequestId(requestMultipartBody.getRequestId());
                connectHandler.send(requestResult);
            });

            return true;
        }
        return false;
    }
}
