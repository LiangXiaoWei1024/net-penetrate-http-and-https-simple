package com.kele.penetrate.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("unused")
public class HandshakeResult implements Serializable
{
    private boolean isSuccess;
    private String accessAddress;
    private String failMessage;
}
