package com.kele.penetrate.protocol;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("unused")
public class HandshakeResult implements Serializable
{
    private boolean isSuccess;
    private String accessAddress;
    private List<String> failMessages;
}
