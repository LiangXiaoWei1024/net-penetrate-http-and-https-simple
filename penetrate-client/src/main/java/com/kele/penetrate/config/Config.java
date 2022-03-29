package com.kele.penetrate.config;

import com.alibaba.fastjson.JSONObject;
import com.kele.penetrate.factory.Recognizer;
import com.kele.penetrate.pojo.DefaultInfo;
import com.kele.penetrate.pojo.ServiceConnectInfo;
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
    private final ServiceConnectInfo serviceConnectInfo = new ServiceConnectInfo();
    private final DefaultInfo defaultInfo = new DefaultInfo();

    public Config()
    {
        String jsonStr = getDataFromFile();
        JSONObject configJson = JSONObject.parseObject(jsonStr);
        JSONObject serviceConnectInfo = configJson.getJSONObject("serviceConnectInfo");
        JSONObject defaultInfo = configJson.getJSONObject("defaultInfo");
        this.serviceConnectInfo.setIp(serviceConnectInfo.getString("ip"));
        this.serviceConnectInfo.setPort(serviceConnectInfo.getInteger("port"));
        this.defaultInfo.setForwardIp(defaultInfo.getString("forwardIp"));
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
