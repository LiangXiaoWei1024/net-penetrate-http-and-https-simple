package com.kele.penetrate.utils.http;


import com.kele.penetrate.protocol.RequestFile;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析http POST请求
 */
@SuppressWarnings("unused")
@Slf4j
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
    public static List<RequestFile> getMultipartBodyFiles(FullHttpRequest fullHttpRequest)
    {
        List<RequestFile> body = new ArrayList<>();
        HttpDataFactory factory = new DefaultHttpDataFactory(true);
        HttpPostRequestDecoder httpDecoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
        httpDecoder.setDiscardThreshold(0);
        httpDecoder.offer(fullHttpRequest);
        List<InterfaceHttpData> interfaceHttpDataList = httpDecoder.getBodyHttpDatas();
        for (InterfaceHttpData data : interfaceHttpDataList)
        {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload)
            {
                FileUpload fileUpload = (FileUpload) data;
                RequestFile requestFile = new RequestFile();
                try
                {
                    requestFile.setFileByte(fileUpload.get());
                    requestFile.setFileName(fileUpload.getFilename());
                    requestFile.setName(fileUpload.getName());
                }
                catch (IOException e)
                {
                    log.error("获取文件异常", e);
                }
                body.add(requestFile);
            }
        }
        return body;
    }

    public static Map<String, String> getMultipartBodyAttribute(FullHttpRequest fullHttpRequest)
    {
        Map<String, String> body = new HashMap<>();
        HttpDataFactory factory = new DefaultHttpDataFactory(true);
        HttpPostRequestDecoder httpDecoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
        httpDecoder.setDiscardThreshold(0);
        httpDecoder.offer(fullHttpRequest);
        List<InterfaceHttpData> interfaceHttpDataList = httpDecoder.getBodyHttpDatas();
        for (InterfaceHttpData data : interfaceHttpDataList)
        {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
            {
                Attribute attribute = (Attribute) data;
                try
                {
                    body.put(attribute.getName(), attribute.getValue());
                }
                catch (IOException e)
                {
                    log.error("获取亲求属性错误", e);
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
