package com.kele.penetrate.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("unused")
public class PostRequestMultipart extends PostRequest
{
    private Map<String, String> bodyMap;
    private List<RequestFile> bodyFile;
}
