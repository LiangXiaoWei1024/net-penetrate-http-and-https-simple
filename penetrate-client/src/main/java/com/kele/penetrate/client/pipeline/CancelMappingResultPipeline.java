package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.page.ClientLogPageManager;
import com.kele.penetrate.page.MainFrame;
import com.kele.penetrate.protocol.CancelMappingResult;
import com.kele.penetrate.utils.Func;

import javax.swing.*;

@Register
@Recognizer
@SuppressWarnings("unused")
public class CancelMappingResultPipeline implements Func<Object, Boolean>
{
    @Autowired
    private ClientLogPageManager clientLogPageManager;
    @Autowired
    private MainFrame mainFrame;

    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof CancelMappingResult)
        {
            clientLogPageManager.addLog("已暂停");
            JButton runButton = mainFrame.getRunButton();
            runButton.setText("启动");
            mainFrame.setAllEditable(true);
            return true;
        }
        return false;
    }
}
