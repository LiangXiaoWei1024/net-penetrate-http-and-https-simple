package com.kele.penetrate;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.BeanFactoryImpl;
import com.kele.penetrate.factory.Recognizer;
import lombok.extern.slf4j.Slf4j;

@Recognizer
@Slf4j
@SuppressWarnings("unused")
public class Start
{
    private static final BeanFactoryImpl beanFactory = new BeanFactoryImpl();

    @Autowired
    private Config config;
    @Autowired
    private ConnectHandler connectHandler;


    public static void main(String[] args)
    {
        Start start = beanFactory.getBean(Start.class);
        start.connectHandler.start();
    }
}
