package com.kele.penetrate.utils;

import lombok.Builder;
import lombok.Data;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Builder
@Data
@SuppressWarnings("unused")
public class HttpUtils
{
    private static final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
    private final OkHttpClient client;

    public HttpUtils()
    {
        clientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        clientBuilder.readTimeout(30, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        client = clientBuilder.build();
    }

    //<editor-fold desc="GET">
    public void get(String url)
    {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.get();
        client.newCall(requestBuilder.build()).enqueue(new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
            {

            }
        });
    }

    public void get(String url, Map<String, String> headers)
    {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.get();
        headers.forEach(requestBuilder::addHeader);

        client.newCall(requestBuilder.build()).enqueue(new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
            {

            }
        });
    }
    //</editor-fold>


}
