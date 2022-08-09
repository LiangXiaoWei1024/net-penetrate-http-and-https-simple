package com.kele.penetrate.client.pipeline;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.page.ClientLogPageManager;
import com.kele.penetrate.page.MainFrame;
import com.kele.penetrate.protocol.CancelResult;
import com.kele.penetrate.utils.Func;

@Register
@Recognizer
@SuppressWarnings("unused")
public class CancelResultPipeline implements Func<Object, Boolean>
{
    @Autowired
    private ClientLogPageManager clientLogPageManager;
    @Autowired
    private MainFrame mainFrame;

    @Override
    public Boolean func(Object msg)
    {
        if (msg instanceof CancelResult)
        {
            clientLogPageManager.addLog("已暂停");
            mainFrame.getRunButton().setText("启动");
            mainFrame.setAllEditable(true);
            return true;
        }
        return false;
    }
}
