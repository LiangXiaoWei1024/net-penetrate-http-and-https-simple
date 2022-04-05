package com.kele.penetrate.protocol;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@SuppressWarnings("unused")
public class HttpPostRequestMultipart extends HttpPostRequest
{
    private Map<String, String> bodyMap;
    private List<RequestFile> bodyFile;
}
