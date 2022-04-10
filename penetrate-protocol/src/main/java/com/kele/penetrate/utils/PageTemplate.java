package com.kele.penetrate.utils;

import com.kele.penetrate.enumeration.ResponseContentType;
import com.kele.penetrate.factory.annotation.Recognizer;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.nio.charset.StandardCharsets;

@Recognizer
public class PageTemplate
{
    private static final String accessDeniedTemplate = "<html><head><title>403 Access Denied(GET请求不支持请求体)</title></head>\n" +
            "<body bgcolor=\"white\">\n" +
            "<center><h1>404 Access Denied (GET请求不支持请求体)</h1></center>\n" +
            "<hr><center>kele</center>\n" +
            "</body></html>";

    private static final String notFound = "<html><head><title>404 Not Found</title></head>\n" +
            "<body bgcolor=\"white\">\n" +
            "<center><h1>404 Not Found</h1></center>\n" +
            "<hr><center>kele</center>\n" +
            "</body></html>";

    private static final String serviceUnavailableTemplate = "<html><head><title>暂时只支持GET|POST|PUT|PATCH|DELETE</title></head>\n" +
            "<body bgcolor=\"white\">\n" +
            "<center><h1>503 暂时只支持GET|POST|PUT|PATCH|DELETE</h1></center>\n" +
            "<hr><center>kele</center>\n" +
            "</body></html>";

    private static final String unableProcessTemplate = "<html><head><title>无法处理的请求(可以联系管理员反馈一下问题，以便下个版本更新)</title></head>\n" +
            "<body bgcolor=\"white\">\n" +
            "<center><h1>无法处理的请求(可以联系管理员反馈一下问题，以便下个版本更新)</h1></center>\n" +
            "<hr><center>kele</center>\n" +
            "</body></html>";


    public  FullHttpResponse getAccessDeniedTemplate()
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.copiedBuffer(accessDeniedTemplate.getBytes(StandardCharsets.UTF_8)));
        response.headers().set("Content-Type", ResponseContentType.TEXT_HTML.code);
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }

    public  FullHttpResponse getNotFoundTemplate()
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.copiedBuffer(notFound.getBytes(StandardCharsets.UTF_8)));
        response.headers().set("Content-Type", ResponseContentType.TEXT_HTML.code);
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }

    public  FullHttpResponse getServiceUnavailableTemplate()
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SERVICE_UNAVAILABLE, Unpooled.copiedBuffer(serviceUnavailableTemplate.getBytes(StandardCharsets.UTF_8)));
        response.headers().set("Content-Type", ResponseContentType.TEXT_HTML.code);
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }

    public  FullHttpResponse getUnableProcess()
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.PRECONDITION_FAILED, Unpooled.copiedBuffer(unableProcessTemplate.getBytes(StandardCharsets.UTF_8)));
        response.headers().set("Content-Type", ResponseContentType.TEXT_HTML.code);
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }


}
