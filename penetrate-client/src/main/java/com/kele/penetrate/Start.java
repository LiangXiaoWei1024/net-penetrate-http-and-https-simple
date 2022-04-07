package com.kele.penetrate;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.BeanFactoryImpl;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.page.ClientLogPageManager;
import com.kele.penetrate.page.MainFrame;
import com.kele.penetrate.page.Tray;
import com.kele.penetrate.utils.Events;
import lombok.extern.slf4j.Slf4j;


@Recognizer
@Slf4j
@SuppressWarnings("unused")
public class Start
{
    private static final BeanFactoryImpl beanFactory = new BeanFactoryImpl();
    public static final Events<Object> clientEvents = new Events("Client", Object.class, "com.kele.penetrate.client.pipeline");

    @Autowired
    private Config config;
    @Autowired
    private ConnectHandler connectHandler;
    @Autowired
    private Tray tray;
    @Autowired
    private MainFrame mainFrame;
    @Autowired
    private ClientLogPageManager clientLogPageManager;


    public static void main(String[] args)
    {
        Start start = BeanFactoryImpl.getBean(Start.class);
        start.mainFrame.init();
        start.clientLogPageManager.addLog("当前版本" + start.config.getVersion());
        start.clientLogPageManager.addLog("如有问题可以联系V:1049705180,QQ群:704592910" + start.config.getVersion());
        start.clientLogPageManager.addLog("请输入信息后启动,也可以用默认配置,但是请映射到本地正确的ip+端口" + start.config.getVersion());
        start.connectHandler.start();

    }
}
