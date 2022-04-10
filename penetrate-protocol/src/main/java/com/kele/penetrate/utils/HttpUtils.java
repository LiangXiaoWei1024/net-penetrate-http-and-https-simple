package com.kele.penetrate.utils;

import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.protocol.RequestFile;
import com.kele.penetrate.protocol.RequestResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLException;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.ConnectException;
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

    /**
     * 不携带请求体的
     *
     * @param requestType 请求类型
     * @param url         地址
     * @param headers     头部信息
     * @param action1     结果回调
     */
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
        RequestResult requestResult = new RequestResult();
        requestResult.setCode(response.code());
        requestResult.setSuccess(true);
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

        if (exception instanceof SSLException)
        {
            requestResult.setFailMessage("Unsupported or unrecognized SSL message");
            requestResult.setCode(401);
        }
        action1.action(requestResult);
    }
    //</editor-fold>
}
