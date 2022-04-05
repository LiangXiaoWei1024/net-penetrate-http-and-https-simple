package com.kele.penetrate.config;

import com.alibaba.fastjson.JSONObject;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.pojo.VersionInfo;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


@Data
@SuppressWarnings("unused")
@Recognizer
public class Config
{
    private int servicePort;
    private int httpPort;
    private int httpsPort;
    private VersionInfo versionInfo = new VersionInfo();

    public Config()
    {
        String jsonStr = getDataFromFile();
        JSONObject configJson = JSONObject.parseObject(jsonStr);
        JSONObject versionInfo = configJson.getJSONObject("versionInfo");
        servicePort = configJson.getJSONObject("service").getInteger("port");
        httpPort = configJson.getJSONObject("http").getInteger("port");
        httpsPort = configJson.getJSONObject("https").getInteger("port");

        this.versionInfo.setVersion(versionInfo.getString("version"));
        this.versionInfo.setContent(versionInfo.getString("content"));
    }

    private String getDataFromFile()
    {
        BufferedReader reader = null;
        StringBuilder latest = new StringBuilder();
        InputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        try
        {
            fileInputStream = Config.class.getClassLoader().getResourceAsStream("config-dev.json");
            assert fileInputStream != null;
            inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);
            String tempString;
            while ((tempString = reader.readLine()) != null)
            {
                latest.append(tempString);
            }
            inputStreamReader.close();
            fileInputStream.close();
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            if (inputStreamReader != null)
            {
                try
                {
                    inputStreamReader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            if (fileInputStream != null)
            {
                try
                {
                    fileInputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return latest.toString();
    }
}
