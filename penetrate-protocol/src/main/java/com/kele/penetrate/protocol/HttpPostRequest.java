package com.kele.penetrate.protocol;

import lombok.Data;

import java.util.Map;

@Data
@SuppressWarnings("unused")
public class HttpPostRequest extends BaseRequest
{
    private Map<String,String> headers;
}
