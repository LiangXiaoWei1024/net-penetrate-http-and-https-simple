package com.kele.penetrate.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class Handshake implements Serializable
{
    private String mappingName;
    private String mappingIp;
    private String port;
}
