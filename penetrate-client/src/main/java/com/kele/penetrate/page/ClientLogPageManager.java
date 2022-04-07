package com.kele.penetrate.page;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户端日志页面管理
 */
@Recognizer
@SuppressWarnings("unused")
public class ClientLogPageManager
{
    @Autowired
    private MainFrame mainFrame;

    private static final List<String> logList = new ArrayList<>();

    public synchronized void addLog(String log)
    {
        if (logList.size() > 100)
        {
            logList.remove(0);
        }
        logList.add(log);
        mainFrame.setLogTextArea(logList);
    }

    public void addLog(List<String> logs)
    {
        if (logs != null)
        {
            for (String log : logs)
            {
                addLog(log);
            }
        }
    }

}
