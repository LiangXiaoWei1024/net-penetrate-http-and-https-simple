package com.kele.penetrate.protocol;

import lombok.Data;

import java.util.Map;

@Data
@SuppressWarnings("unused")
public class HttpGetRequestResult extends BaseRequest
{
    private Map<String,String> headers;
    private byte[] data;
}
