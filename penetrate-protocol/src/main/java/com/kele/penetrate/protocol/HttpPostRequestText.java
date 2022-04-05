package com.kele.penetrate.protocol;

import lombok.Data;

@Data
@SuppressWarnings("unused")
public class HttpPostRequestText extends HttpPostRequest
{
    private String dataText;
}
