package com.kele.penetrate.config;

import com.alibaba.fastjson.JSONObject;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.pojo.ServiceConnectInfo;
import com.kele.penetrate.utils.FileUtils;
import lombok.Data;

@Data
@Recognizer
@SuppressWarnings("unused")
public class Config
{
    private final ServiceConnectInfo serviceConnectInfo = new ServiceConnectInfo();
    private String version;

    public Config()
    {
        String jsonStr = FileUtils.getDataFromFile(Config.class.getClassLoader().getResourceAsStream("config-dev.json"));
        JSONObject configJson = JSONObject.parseObject(jsonStr);
        JSONObject serviceConnectInfo = configJson.getJSONObject("serviceConnectInfo");
        JSONObject defaultInfo = configJson.getJSONObject("defaultInfo");
        this.serviceConnectInfo.setIp(serviceConnectInfo.getString("ip"));
        this.serviceConnectInfo.setPort(serviceConnectInfo.getInteger("port"));
        this.version = configJson.getJSONObject("version").getString("value");
    }
}
