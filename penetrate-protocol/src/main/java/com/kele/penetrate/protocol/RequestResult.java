package com.kele.penetrate.protocol;

import lombok.Data;

import java.util.Map;

@Data
@SuppressWarnings("unused")
public class RequestResult extends BaseRequest
{
    private int code;
    private byte[] data;
    private Map<String,String> headers;
    private boolean isSuccess;
    private String failMessage;
}
