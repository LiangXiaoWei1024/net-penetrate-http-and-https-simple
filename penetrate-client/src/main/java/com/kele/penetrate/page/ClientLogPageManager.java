package com.kele.penetrate.page;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户端日志页面管理
 */
@Recognizer
@SuppressWarnings("unused")
public class ClientLogPageManager
{
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Autowired
    private MainFrame mainFrame;


    class LogInfo
    {
        private String time;
        private String msg;

        public String getTime()
        {
            return time;
        }

        public void setTime(String time)
        {
            this.time = time;
        }

        public String getMsg()
        {
            return msg;
        }

        public void setMsg(String msg)
        {
            this.msg = msg;
        }
    }

    private static final List<LogInfo> logList = new ArrayList<>();

    public synchronized void addLog(String log)
    {
        if (logList.size() > 100)
        {
            logList.remove(0);
        }
        LogInfo logInfo = new LogInfo();
        logInfo.setMsg(log);
        logInfo.setTime(sdf.format(System.currentTimeMillis()));
        logList.add(logInfo);
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
