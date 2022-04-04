package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.Recognizer;
import com.kele.penetrate.factory.Register;
import com.kele.penetrate.protocol.HttpGetRequest;
import com.kele.penetrate.protocol.HttpGetRequestResult;
import com.kele.penetrate.utils.Func;
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

    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof HttpGetRequest)
        {
            HttpGetRequest httpGetRequest = (HttpGetRequest) msg;
            System.out.println("收到httpGET->"+httpGetRequest);
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url(httpGetRequest.getRequestUrl());
            builder.get();
            if (httpGetRequest.getHeaders() != null)
            {
                httpGetRequest.getHeaders().forEach((k, v) ->
                {
                    builder.addHeader(k, v);
                });
            }
            Call call = client.newCall(builder.build());
            try
            {
                Response execute = call.execute();
                Headers headers = execute.headers();
                Map<String, String> h = new HashMap<>();
                Iterator<Pair<String, String>> iterator = headers.iterator();
                while (iterator.hasNext())
                {
                    Pair<String, String> next = iterator.next();
                    h.put(next.getFirst(), next.getSecond());
                }
                byte[] bytes = execute.body().bytes();

                HttpGetRequestResult httpGetRequestResult = new HttpGetRequestResult();
                httpGetRequestResult.setRequestId(httpGetRequest.getRequestId());
                httpGetRequestResult.setData(bytes);
                httpGetRequestResult.setHeaders(h);
                connectHandler.send(httpGetRequestResult);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


            return true;
        }
        return false;
    }
}
