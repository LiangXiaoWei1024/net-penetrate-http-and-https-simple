package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.Recognizer;
import com.kele.penetrate.factory.Register;
import com.kele.penetrate.protocol.HttpGetRequest;
import com.kele.penetrate.protocol.HttpGetRequestResult;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.HttpUtils;
import kotlin.Pair;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
            System.out.println("收到httpGET->" + httpGetRequest);

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
