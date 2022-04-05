package com.kele.penetrate.pojo;

import lombok.Data;

/**
 * 连接服务端配置信息
 */
@Data
@SuppressWarnings("unused")
public class ServiceConnectInfo
{
    private String ip;
    private Integer port;
}
