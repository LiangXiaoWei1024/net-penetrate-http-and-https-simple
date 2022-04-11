package com.kele.penetrate.utils;

import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.protocol.RequestFile;
import com.kele.penetrate.protocol.RequestResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
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
    private final X509TrustManager manager = SSLSocketClientUtil.getX509TrustManager();

    public HttpUtils()
    {
        //设置超时时间
        clientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        clientBuilder.readTimeout(60, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS);
        clientBuilder.sslSocketFactory(SSLSocketClientUtil.getSocketFactory(manager), manager);
        clientBuilder.hostnameVerifier(SSLSocketClientUtil.getHostnameVerifier());
        client = clientBuilder.build();
    }

    //<editor-fold desc="Not Body">
    public void requestNotBody(RequestType requestType, String url, Map<String, String> headers, Action1<RequestResult> action1)
    {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        if (requestType == RequestType.GET)
        {
            requestBuilder.get();
        }
        else
        {
            requestBuilder.method(requestType.code, RequestBody.create("", null));
        }
        headers.forEach(requestBuilder::addHeader);
        execute(requestBuilder.build(), action1);
    }
    //</editor-fold>

    //<editor-fold desc="Form Body">
    public void requestFormBody(RequestType requestType, String url, Map<String, String> headers, Map<String, String> formBody, Action1<RequestResult> action1)
    {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBody.forEach(formBodyBuilder::add);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.method(requestType.code, formBodyBuilder.build());
        headers.forEach(requestBuilder::addHeader);
        execute(requestBuilder.build(), action1);
    }
    //</editor-fold>

    //<editor-fold desc="Form Text">
    public void requestTextBody(RequestType requestType, String url, Map<String, String> headers, String textBody, Action1<RequestResult> action1)
    {
        RequestBody body = RequestBody.create(textBody, MediaType.parse(headers.get("Content-Type")));
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(body);
        headers.forEach(requestBuilder::addHeader);
        execute(requestBuilder.build(), action1);
    }
    //</editor-fold>

    //<editor-fold desc="Form Multipart">
    public void requestMultipartBody(RequestType requestType, String url, Map<String, String> headers, Map<String, String> bodyMap, List<RequestFile> bodyFiles, Action1<RequestResult> action1)
    {
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        bodyMap.forEach(multipartBodyBuilder::addFormDataPart);

        if (bodyFiles != null)
        {
            for (RequestFile requestFile : bodyFiles)
            {
                multipartBodyBuilder.addFormDataPart(requestFile.getName(), requestFile.getFileName(), RequestBody.create(requestFile.getFileByte(), MediaType.parse("multipart/form-data")));
            }
        }

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.method(requestType.code, multipartBodyBuilder.build());
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
        Response priorResponse = response.priorResponse();
        RequestResult requestResult = new RequestResult();
        Response resultResponse = null;


        if (priorResponse != null)
        {
            int priorResponseCode = priorResponse.code();
            if (priorResponseCode == HttpURLConnection.HTTP_MOVED_PERM || priorResponseCode == HttpURLConnection.HTTP_MOVED_TEMP)
            {
                resultResponse = priorResponse;
            }
        }
        else
        {
            resultResponse = response;
            try
            {
                requestResult.setData(Objects.requireNonNull(response.body()).bytes());
            }
            catch (IOException e)
            {
                log.error("获取数据异常", e);
            }
        }

        if (resultResponse != null)
        {
            requestResult.setCode(resultResponse.code());
            requestResult.setSuccess(true);
            Map<String, String> headers = new HashMap<>();
            resultResponse.headers().forEach(pair -> headers.put(pair.getFirst(), pair.getSecond()));
            requestResult.setHeaders(headers);
        }

        action1.action(requestResult);
    }

    private void failResultHandle(IOException exception, Action1<RequestResult> action1)
    {
        RequestResult requestResult = new RequestResult();

        if (exception != null)
        {
            requestResult.setFailMessage(exception.getMessage());
            if (exception instanceof ConnectException)
            {
                requestResult.setCode(504);
            }
            else if (exception instanceof SocketTimeoutException)
            {
                requestResult.setFailMessage(exception.getMessage());
                requestResult.setCode(408);
            }
            else
            {
                requestResult.setCode(0);
            }
        }
        else
        {
            requestResult.setFailMessage("Unknown exception");
            requestResult.setCode(0);
        }

        action1.action(requestResult);
    }
    //</editor-fold>
}
