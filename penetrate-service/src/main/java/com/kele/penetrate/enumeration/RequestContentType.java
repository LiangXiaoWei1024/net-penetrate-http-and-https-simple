package com.kele.penetrate.enumeration;


@SuppressWarnings("unused")
public enum RequestContentType
{
    MULTIPART_FORM_DATA("multipart/form-data"),
    X_WWW_FORM_URLENCODED("x-www-form-urlencoded"),
    TEXT_PLAIN("text"),
    APPLICATION_JSON("application/json"),
    APPLICATION_JAVASCRIPT("application/javascript"),
    APPLICATION_XML("application/xml"),

    ;
    private final String code;

    RequestContentType(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
}
