package com.kele.penetrate.pojo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;


@Data
@SuppressWarnings("unused")
public class VersionInfo
{
    private String version;
    private JSONArray contents;
}
