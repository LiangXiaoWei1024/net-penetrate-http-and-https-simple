package com.kele.penetrate.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;


@Data
@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true)
public class GetRequest extends BaseRequest
{
    private String requestUrl;
    private Map<String,String> headers;
}
