package com.kele.penetrate.utils.http;

import com.kele.penetrate.enumeration.RequestType;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 解析http&https请求
 */
@SuppressWarnings("unused")
@Slf4j
public class AnalysisRequest
{
    //<editor-fold desc="获取请求方法类型">
    public static RequestType getRequestType(FullHttpRequest fullHttpRequest)
    {
        return RequestType.getRequestTypeByCodeStr(fullHttpRequest.method().name().trim());
    }
    //</editor-fold>

    //<editor-fold desc="获取请求url携带的参数">
    public static Map<String, Object> getParamsFromChannel(FullHttpRequest fullHttpRequest)
    {
        Map<String, Object> params = new HashMap<>();
        QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
        Map<String, List<String>> paramList = decoder.parameters();
        for (Map.Entry<String, List<String>> entry : paramList.entrySet())
        {
            params.put(entry.getKey(), entry.getValue().get(0));
        }
        return params;
    }
    //</editor-fold>

    //<editor-fold desc="获取请求头信息">
    public static Map<String, String> getRequestHeaders(FullHttpRequest fullHttpRequest)
    {
        Map<String, String> headers = new HashMap<>();
        Iterator<Map.Entry<String, String>> entryIterator = fullHttpRequest.headers().iteratorAsString();
        while (entryIterator.hasNext())
        {
            Map.Entry<String, String> next = entryIterator.next();
            headers.put(next.getKey(), next.getValue());
        }
        return headers;
    }
    //</editor-fold>

    //<editor-fold desc="获取归属用户">
    public static String getHomeUser(FullHttpRequest fullHttpRequest)
    {
        try
        {
            return fullHttpRequest.uri().split("/")[1];
        }
        catch (Exception ex)
        {
            log.error("没有用户");
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold desc="获取请求路径">
    public static String getRequestUrl(FullHttpRequest fullHttpRequest,boolean isFilterMappingName)
    {
        StringBuilder uri = new StringBuilder(fullHttpRequest.uri());
        if(isFilterMappingName){
            String[] split = uri.toString().split("/");
            uri = new StringBuilder();
            for (int i = 1; i < split.length; i++)
            {
                uri.append(split[i]);
            }
        }
        return uri.toString();
    }
    //</editor-fold>
}
