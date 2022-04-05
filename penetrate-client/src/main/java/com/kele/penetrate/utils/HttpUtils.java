package com.kele.penetrate.utils;

import com.kele.penetrate.factory.Recognizer;
import com.kele.penetrate.protocol.RequestResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Data
@SuppressWarnings("unused")
@Slf4j
@Recognizer
public class HttpUtils
{
    private static final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
    private final OkHttpClient client;

    public HttpUtils()
    {
        //设置超时时间
        clientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        clientBuilder.readTimeout(6, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(6, TimeUnit.SECONDS);
        client = clientBuilder.build();
    }

    //<editor-fold desc="GET">
    public void get(String url, Action1<RequestResult> action1)
    {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.get();
        execute(requestBuilder.build(), action1);
    }

    public void get(String url, Map<String, String> headers, Action1<RequestResult> action1)
    {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.get();
        headers.forEach(requestBuilder::addHeader);
        execute(requestBuilder.build(), action1);
    }
    //</editor-fold>

    //<editor-fold desc="执行">
    private void execute(Request request, Action1<RequestResult> action1)
    {
        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                failResultHandle(e, action1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
            {
                successResultHandle(response, action1);
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="数据处理">
    private void successResultHandle(Response response, Action1<RequestResult> action1)
    {
        RequestResult requestResult = new RequestResult();
        requestResult.setCode(response.code());
        Map<String, String> headers = new HashMap<>();
        response.headers().forEach(pair -> headers.put(pair.getFirst(), pair.getSecond()));
        requestResult.setHeaders(headers);
        try
        {
            requestResult.setData(Objects.requireNonNull(response.body()).bytes());
        }
        catch (IOException e)
        {
            log.error("获取数据异常", e);
        }
        action1.action(requestResult);
    }

    private void failResultHandle(IOException exception, Action1<RequestResult> action1)
    {
        RequestResult requestResult = new RequestResult();
        if (exception instanceof ConnectException)
        {
            requestResult.setFailMessage("connect timeout");
            requestResult.setCode(408);
        }
        if (exception instanceof SocketTimeoutException)
        {
            requestResult.setFailMessage("request timeout");
            requestResult.setCode(408);
        }
        action1.action(requestResult);
    }
    //</editor-fold>
}
