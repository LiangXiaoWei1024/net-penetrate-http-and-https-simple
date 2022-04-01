package com.kele.penetrate.receiver.http;

import com.kele.penetrate.enumeration.RequestType;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 解析http请求
 */
public class AnalysisHttpRequest
{

    //<editor-fold desc="获取请求方法类型">
    public static RequestType getRequestType(FullHttpRequest fullHttpRequest)
    {
        return RequestType.getRequestTypeByCodeStr(fullHttpRequest.method().name().trim());
    }
    //</editor-fold>
}
