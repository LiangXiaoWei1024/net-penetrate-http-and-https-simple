package com.kele.penetrate.protocol;

import lombok.Data;

import java.util.Map;

@Data
public class HttpGetRequestResult extends BaseRequest
{
    private Map<String,String> headers;
    private byte[] data;
}
