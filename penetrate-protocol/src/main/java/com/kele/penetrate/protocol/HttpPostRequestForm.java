package com.kele.penetrate.protocol;

import lombok.Data;

import java.util.Map;

@Data
@SuppressWarnings("unused")
public class HttpPostRequestForm extends HttpPostRequest
{
    private Map<String, String> dataBody;
}
