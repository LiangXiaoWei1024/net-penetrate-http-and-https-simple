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
@SuppressWarnings("unused")
public class PageTemplate
{

    private static final String GET_BODY_ACCESS_DENIED = "403 Access Denied(GET请求不支持请求体)";
    private static final String NOT_FOUND = "404 Not Found";
    private static final String SERVICE_UNAVAILABLE = "只支持GET|POST|PUT|PATCH|DELETE";
    private static final String UNABLE_PROCESS = "Content-Type类型是无法处理的请求类型,请看一下请求头的信息,(可以联系管理员反馈一下问题，以便下个版本更新)";


    public FullHttpResponse get_GetBodyAccessDenied_Template()
    {
        return createTemplate(GET_BODY_ACCESS_DENIED, GET_BODY_ACCESS_DENIED, HttpResponseStatus.FORBIDDEN);
    }

    public FullHttpResponse get_NotFound_Template()
    {
        return createTemplate(NOT_FOUND, NOT_FOUND, HttpResponseStatus.NOT_FOUND);
    }

    public FullHttpResponse get_ServiceUnavailable_Template()
    {
        return createTemplate(SERVICE_UNAVAILABLE, SERVICE_UNAVAILABLE, HttpResponseStatus.SERVICE_UNAVAILABLE);
    }

    public FullHttpResponse get_UnableProcess_Template()
    {
        return createTemplate(UNABLE_PROCESS, UNABLE_PROCESS, HttpResponseStatus.PRECONDITION_FAILED);
    }

    public FullHttpResponse createTemplate(String title, String msg, HttpResponseStatus responseStatus)
    {
        String template = "<html><head><title>" + title + "</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>" + msg + "</h1></center>\n" +
                "<hr><center>kele</center>\n" +
                "</body></html>";
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, responseStatus, Unpooled.copiedBuffer(template.getBytes(StandardCharsets.UTF_8)));

        response.headers().set("Content-Type", ResponseContentType.TEXT_HTML.code);
        response.headers().set("Content_Length", response.content().readableBytes());

        return response;
    }


}
