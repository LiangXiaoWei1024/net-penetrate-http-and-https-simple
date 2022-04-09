package com.kele.penetrate.utils;

import com.kele.penetrate.factory.annotation.Recognizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
public class FileUtils
{
    public static String getDataFromFile(InputStream fileInputStream)
    {
        BufferedReader reader = null;
        StringBuilder latest = new StringBuilder();
        InputStreamReader inputStreamReader = null;
        try
        {
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
