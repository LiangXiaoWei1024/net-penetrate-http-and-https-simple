package com.kele.penetrate.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class PostRequestForm extends PostRequest
{
    private Map<String, String> dataBody;
}
