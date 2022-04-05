package com.kele.penetrate.utils.http;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析http POST请求
 */
@SuppressWarnings("unused")
public class AnalysisHttpPostRequest extends AnalysisRequest
{

    //<editor-fold desc="获取请求体 x-www-form-urlencoded">
    public static Map<String, String> getFormBody(FullHttpRequest fullHttpRequest)
    {
        Map<String, String> body = new HashMap<>();
        HttpDataFactory factory = new DefaultHttpDataFactory(false);
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
        for (InterfaceHttpData data : postData)
        {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
            {
                MemoryAttribute attribute = (MemoryAttribute) data;
                body.put(attribute.getName(), attribute.getValue());
            }
        }
        return body;
    }
    //</editor-fold>

    //<editor-fold desc="获取请求体 multipart/form-data">
    public static Map<String, String> getMultipartBody(FullHttpRequest fullHttpRequest)
    {
        Map<String, String> body = new HashMap<>();
        HttpDataFactory factory = new DefaultHttpDataFactory(true);
        HttpPostRequestDecoder httpDecoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
        httpDecoder.setDiscardThreshold(0);
        final HttpContent chunk = fullHttpRequest;
        httpDecoder.offer(chunk);
        if (chunk instanceof LastHttpContent)
        {
            List<InterfaceHttpData> interfaceHttpDataList = httpDecoder.getBodyHttpDatas();
            for (InterfaceHttpData data : interfaceHttpDataList)
            {
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload)
                {
                    FileUpload fileUpload = (FileUpload) data;
                    String filename = fileUpload.getFilename();
                    String name = fileUpload.getName();
                    try
                    {
                        byte[] bytes = fileUpload.get();
                        System.out.println(bytes.length);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    System.out.println(filename);
                }
                //如果数据类型为参数类型，则保存到body对象中
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
                {
                    Attribute attribute = (Attribute) data;
                    System.out.println(attribute);
                }
            }
        }
        return body;
    }
    //</editor-fold>


    //<editor-fold desc="获取请求体 application(json xml javaScript),text(plain html)">
    public static String getTextBody(FullHttpRequest fullHttpRequest)
    {
        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        return content.toString(CharsetUtil.UTF_8);
    }
    //</editor-fold>
}
