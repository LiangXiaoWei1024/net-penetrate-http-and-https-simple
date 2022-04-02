package com.kele.penetrate.enumeration;


@SuppressWarnings("unused")
public enum ResponseContentType
{
    TEXT_HTML("text/html;charset=UTF-8"),
    ;
    private final String code;

    ResponseContentType(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
}
