package com.kele.penetrate.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseRequest implements Serializable
{
    private String requestId;
}
