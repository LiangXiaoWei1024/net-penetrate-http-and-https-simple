package com.kele.penetrate.protocol;

import lombok.Data;

import java.util.Map;

@Data
@SuppressWarnings("unused")
public class PostRequestForm extends PostRequest
{
    private Map<String, String> dataBody;
}
