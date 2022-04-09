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
    private static final List<LogInfo> logList = new ArrayList<>();
    private static final int logNumber = 100;

    @Autowired
    private MainFrame mainFrame;

    //<editor-fold desc="內部消息封装类">
    static class LogInfo
    {
        private String time;
        private String msg;

        public LogInfo(String time, String msg)
        {
            this.time = time;
            this.msg = msg;
        }

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
    //</editor-fold>

    //<editor-fold desc="添加日志">
    public synchronized void addLog(String log)
    {
        if (logList.size() > logNumber)
        {
            logList.remove(0);
        }
        logList.add(new LogInfo(sdf.format(System.currentTimeMillis()), log));
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
    //</editor-fold>
}
