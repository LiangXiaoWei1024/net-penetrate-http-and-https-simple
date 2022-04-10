package com.kele.penetrate.enumeration;


@SuppressWarnings("unused")
public enum HypertextTransferProtocolType
{
    HTTP("http"),
    HTTPS("https");
    public final String code;

    HypertextTransferProtocolType(String code)
    {
        this.code = code;
    }
}
