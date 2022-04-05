package com.kele.penetrate.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("unused")
public class Handshake implements Serializable
{
    private String mappingName;
    private String mappingIp;
    private Integer port;
}
