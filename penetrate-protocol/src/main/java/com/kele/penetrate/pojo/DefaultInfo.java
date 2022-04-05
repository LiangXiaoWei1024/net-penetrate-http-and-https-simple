package com.kele.penetrate.pojo;

import lombok.Data;

/**
 * 默认配置信息
 */
@SuppressWarnings("unused")
@Data
public class DefaultInfo
{
    /**
     * 默认转发ip
     */
    private String forwardIp;
    /**
     * 默认转发端口
     */
    private Integer port;
}
