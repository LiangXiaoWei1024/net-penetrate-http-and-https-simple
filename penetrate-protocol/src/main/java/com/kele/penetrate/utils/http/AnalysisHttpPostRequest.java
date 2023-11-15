package com.kele.penetrate.utils.http;


import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.pojo.MultipartBody;
import com.kele.penetrate.protocol.RequestFile;
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
@Recognizer
public class AnalysisHttpPostRequest extends AnalysisRequest
{

    //<editor-fold desc="获取请求体 x-www-form-urlencoded">
    public Map<String, String> getFormBody(FullHttpRequest fullHttpRequest)
    {
        Map<String, String> body = new HashMap<>();
        HttpDataFactory factory = new DefaultHttpDataFactory(false);
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
        try
        {
            List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
            for (InterfaceHttpData data : postData)
            {
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
                {
                    MemoryAttribute attribute = (MemoryAttribute) data;
                    body.put(attribute.getName(), attribute.getValue());
                }
            }
        }
        finally
        {
            // 确保释放资源
            decoder.destroy();
        }
        return body;
    }
    //</editor-fold>

    //<editor-fold desc="获取请求体 multipart/form-data">
    public MultipartBody getMultipartBody(FullHttpRequest fullHttpRequest)
    {
        Map<String, String> bodyMap = new HashMap<>();
        List<RequestFile> bodyFiles = new ArrayList<>();

        MultipartBody body = new MultipartBody();
        body.setBodyMap(bodyMap);
        body.setBodyFiles(bodyFiles);

        HttpDataFactory factory = new DefaultHttpDataFactory(true);
        HttpPostRequestDecoder httpDecoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
        httpDecoder.setDiscardThreshold(0);
        httpDecoder.offer(fullHttpRequest);
        try
        {
            List<InterfaceHttpData> interfaceHttpDataList = httpDecoder.getBodyHttpDatas();
            for (InterfaceHttpData data : interfaceHttpDataList)
            {
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
                {
                    Attribute attribute = (Attribute) data;
                    try
                    {
                        bodyMap.put(attribute.getName(), attribute.getValue());
                    }
                    catch (IOException e)
                    {
                        log.error("获取请求属性错误", e);
                    }
                }
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
                    bodyFiles.add(requestFile);
                }
            }
        }
        finally
        {
            // 确保释放资源
            httpDecoder.destroy();
        }
        return body;
    }
    //</editor-fold>

    //<editor-fold desc="获取请求体 application(json xml javaScript),text(plain html)">
    public String getTextBody(FullHttpRequest fullHttpRequest)
    {
        return fullHttpRequest.content().toString(CharsetUtil.UTF_8);
    }
    //</editor-fold>
}
