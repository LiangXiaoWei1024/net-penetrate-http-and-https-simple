package com.kele.penetrate.utils;


import com.kele.penetrate.factory.annotation.Recognizer;

import java.io.*;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
@Recognizer
public class FileUtils
{
    /**
     * 根目录
     */
    public String rootDirectory = System.getProperty("user.dir");
    public String recordOperation = "recordOperation.json";

    /**
     * 写入本地文件
     *
     * @param str      内容
     * @param path     路径
     * @param fileName 文件名称
     */
    public void writeLocalStr(String str, String path, String fileName)
    {
        try
        {
            File file = new File(path + "/" + fileName);
            if (!file.exists())
            {
                boolean mkdirs = file.createNewFile();
            }

            if (str != null && !"".equals(str))
            {
                FileWriter fw = new FileWriter(file);
                fw.write(str);
                fw.flush();
                fw.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 读取本地文件
     *
     * @param filePath 文件路径
     * @return 结果
     */
    public String readFileStr(String filePath)
    {
        BufferedReader bf = null;
        StringBuilder buffer = null;
        try
        {
            File file = new File(filePath);
            if (file.exists())
            {
                buffer = new StringBuilder();
                bf = new BufferedReader(new FileReader(filePath));
                String data;
                while ((data = bf.readLine()) != null)
                {//使用readLine方法，一次读一行
                    buffer.append(data);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if (bf != null)
                {
                    bf.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (buffer == null)
        {
            return null;
        }
        return buffer.toString();
    }

    /**
     * 读取jar中的文件
     *
     * @param fileInputStream 流
     * @return 结果
     */
    public String getDataFromFile(InputStream fileInputStream)
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
