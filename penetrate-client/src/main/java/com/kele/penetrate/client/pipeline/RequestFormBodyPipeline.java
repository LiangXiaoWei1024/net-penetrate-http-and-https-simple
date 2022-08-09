package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.page.MainFrame;
import com.kele.penetrate.protocol.RequestFormBody;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.HttpUtils;

@Register
@Recognizer
@SuppressWarnings("unused")
public class RequestFormBodyPipeline implements Func<Object, Boolean>
{
    @Autowired
    private ConnectHandler connectHandler;
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private MainFrame mainFrame;

    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof RequestFormBody)
        {
            RequestFormBody requestFormBody = (RequestFormBody) msg;
            String requestUrl = requestFormBody.getRequestProtocolType().code + "://" + mainFrame.getIp() + ":" + mainFrame.getPort() + "" + requestFormBody.getRequestUri();

            httpUtils.requestFormBody(requestFormBody.getRequestType(), requestUrl, requestFormBody.getHeaders(), requestFormBody.getDataBody(), requestResult ->
            {
                requestResult.setRequestId(requestFormBody.getRequestId());
                connectHandler.send(requestResult);
            });
            return true;
        }
        return false;
    }
}
