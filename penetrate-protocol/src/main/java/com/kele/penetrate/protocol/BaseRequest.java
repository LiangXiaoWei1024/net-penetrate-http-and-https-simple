package com.kele.penetrate.protocol;

import com.kele.penetrate.enumeration.HypertextTransferProtocolType;
import com.kele.penetrate.enumeration.RequestType;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class BaseRequest implements Serializable
{
    private String requestId;
    private HypertextTransferProtocolType requestProtocolType;
    private String requestUri;
    private Map<String, String> headers;
    private RequestType requestType;
}
