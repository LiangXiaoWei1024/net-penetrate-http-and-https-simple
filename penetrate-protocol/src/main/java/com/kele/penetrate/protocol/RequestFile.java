package com.kele.penetrate.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("unused")
public class RequestFile implements Serializable
{
    private String name;
    private String fileName;
    private byte[] fileByte;
}
