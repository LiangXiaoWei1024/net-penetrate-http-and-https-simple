package com.kele.penetrate.enumeration;


@SuppressWarnings("unused")
public enum HypertextTransferProtocolType
{
    HTTP("http"),
    HTTPS("https");
    private final String code;

    HypertextTransferProtocolType(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
}
