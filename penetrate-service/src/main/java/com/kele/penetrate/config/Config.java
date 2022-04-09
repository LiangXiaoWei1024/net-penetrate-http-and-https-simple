package com.kele.penetrate.config;

import com.alibaba.fastjson.JSONObject;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.pojo.VersionInfo;
import lombok.Data;
import com.kele.penetrate.utils.FileUtils;


@Data
@Recognizer
@SuppressWarnings("unused")
public class Config
{
    private int servicePort;
    private int httpPort;
    private int httpsPort;
    private VersionInfo versionInfo = new VersionInfo();

    public Config()
    {
        String jsonStr = FileUtils.getDataFromFile(Config.class.getClassLoader().getResourceAsStream("config-dev.json"));
        JSONObject configJson = JSONObject.parseObject(jsonStr);
        JSONObject versionInfo = configJson.getJSONObject("versionInfo");
        servicePort = configJson.getJSONObject("service").getInteger("port");
        httpPort = configJson.getJSONObject("http").getInteger("port");
        httpsPort = configJson.getJSONObject("https").getInteger("port");

        this.versionInfo.setVersion(versionInfo.getString("version"));
        this.versionInfo.setContents(versionInfo.getJSONArray("contents"));
    }
}
