package com.kele.penetrate.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true)
public class PostRequest extends BaseRequest
{
    private Map<String, String> headers;
}
