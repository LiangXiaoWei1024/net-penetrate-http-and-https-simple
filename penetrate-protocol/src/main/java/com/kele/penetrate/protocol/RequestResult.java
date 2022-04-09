package com.kele.penetrate.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
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
