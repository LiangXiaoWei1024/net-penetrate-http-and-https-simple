package com.kele.penetrate.config;

import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.pojo.ServiceConnectInfo;
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
    private final ServiceConnectInfo serviceConnectInfo = new ServiceConnectInfo();
    private String version;
    private String domainName;

    public Config() throws IOException
    {
        Properties properties = new Properties();
        properties.load(new BufferedReader(new InputStreamReader(Objects.requireNonNull(Config.class.getClassLoader().getResourceAsStream("config.properties")))));
        this.version = properties.getProperty("version");
        this.serviceConnectInfo.setIp(properties.getProperty("service.ip"));
        this.serviceConnectInfo.setPort(Integer.parseInt(properties.getProperty("service.port")));
        this.domainName = properties.getProperty("domain.name");
    }
}
