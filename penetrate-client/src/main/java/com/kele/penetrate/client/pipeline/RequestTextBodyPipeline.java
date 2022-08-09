package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.page.MainFrame;
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
    @Autowired
    private MainFrame mainFrame;


    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof RequestTextBody)
        {
            RequestTextBody requestTextBody = (RequestTextBody) msg;
            String requestUrl = requestTextBody.getRequestProtocolType().code + "://" + mainFrame.getIp() + ":" + mainFrame.getPort() + "" + requestTextBody.getRequestUri();

            httpUtils.requestTextBody(requestTextBody.getRequestType(), requestUrl, requestTextBody.getHeaders(), requestTextBody.getDataText(), requestResult ->
            {
                requestResult.setRequestId(requestTextBody.getRequestId());
                connectHandler.send(requestResult);
            });
            return true;
        }
        return false;
    }
}
