package com.kele.penetrate.config;

import com.alibaba.fastjson.JSONArray;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.pojo.MySSL;
import com.kele.penetrate.pojo.VersionInfo;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;


@Data
@Recognizer
@SuppressWarnings("unused")
public class Config
{
    private int startPort;
    private int httpPort;
    private int httpsPort;
    private VersionInfo versionInfo = new VersionInfo();
    private MySSL mySSL = new MySSL();

    public Config() throws IOException
    {

        Properties properties = new Properties();
        properties.load(new BufferedReader(new InputStreamReader(Objects.requireNonNull(Config.class.getClassLoader().getResourceAsStream("config.properties")))));

        this.httpPort = Integer.parseInt(properties.getProperty("http.port"));
        this.httpsPort = Integer.parseInt(properties.getProperty("https.port"));
        this.startPort = Integer.parseInt(properties.getProperty("start.port"));

        this.versionInfo.setVersion(properties.getProperty("version"));
        this.versionInfo.setContents(JSONArray.parseArray(properties.getProperty("version.contents")));

        mySSL.setPassword(properties.getProperty("ssl.password"));
        mySSL.setName(properties.getProperty("ssl.name"));
    }
}
