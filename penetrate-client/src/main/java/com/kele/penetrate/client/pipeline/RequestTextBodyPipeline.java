package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.protocol.RequestTextBody;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.HttpUtils;

@Register
@Recognizer
@SuppressWarnings("unused")
public class RequestTextBodyPipeline implements Func<Object, Boolean>
{

    @Autowired
    private ConnectHandler connectHandler;
    @Autowired
    private HttpUtils httpUtils;


    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof RequestTextBody)
        {
            RequestTextBody requestTextBody = (RequestTextBody) msg;
            httpUtils.requestTextBody(requestTextBody.getRequestType(), requestTextBody.getRequestUrl(), requestTextBody.getHeaders(), requestTextBody.getDataText(), requestResult ->
            {
                requestResult.setRequestId(requestTextBody.getRequestId());
                connectHandler.send(requestResult);
            });
            return true;
        }
        return false;
    }
}
