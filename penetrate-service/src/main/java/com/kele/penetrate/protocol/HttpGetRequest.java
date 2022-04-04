package com.kele.penetrate.protocol;

import lombok.Data;

import java.util.Map;


@Data
public class HttpGetRequest extends BaseRequest
{
    private String requestUrl;
    private Map<String,String> headers;
}
