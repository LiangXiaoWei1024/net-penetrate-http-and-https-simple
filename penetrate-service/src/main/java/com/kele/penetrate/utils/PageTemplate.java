package com.kele.penetrate.utils;

import com.kele.penetrate.enumeration.ResponseContentType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.nio.charset.StandardCharsets;

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

    private static final String serviceUnavailableTemplate = "<html><head><title>503 暂时只支持GET|POST</title></head>\n" +
            "<body bgcolor=\"white\">\n" +
            "<center><h1>503 暂时只支持GET|POST</h1></center>\n" +
            "<hr><center>kele</center>\n" +
            "</body></html>";


    public static FullHttpResponse getAccessDeniedTemplate()
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.copiedBuffer(accessDeniedTemplate.getBytes(StandardCharsets.UTF_8)));
        response.headers().set("Content-Type", ResponseContentType.TEXT_HTML.getCode());
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }

    public static FullHttpResponse getNotFoundTemplate()
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.copiedBuffer(notFound.getBytes(StandardCharsets.UTF_8)));
        response.headers().set("Content-Type", ResponseContentType.TEXT_HTML.getCode());
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }

    public static FullHttpResponse getServiceUnavailableTemplate()
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SERVICE_UNAVAILABLE, Unpooled.copiedBuffer(notFound.getBytes(StandardCharsets.UTF_8)));
        response.headers().set("Content-Type", ResponseContentType.TEXT_HTML.getCode());
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }

}
