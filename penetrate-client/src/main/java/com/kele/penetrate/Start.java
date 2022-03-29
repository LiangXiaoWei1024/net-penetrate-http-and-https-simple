package com.kele.penetrate;

import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.BeanFactoryImpl;
import com.kele.penetrate.factory.Recognizer;
import com.kele.penetrate.pojo.ServiceConnectInfo;
import lombok.extern.slf4j.Slf4j;

@Recognizer
@Slf4j
@SuppressWarnings("unused")
public class Start
{
    private static final BeanFactoryImpl beanFactory = new BeanFactoryImpl();

    @Autowired
    private Config config;


    public static void main(String[] args)
    {
        Start start = beanFactory.getBean(Start.class);
        ServiceConnectInfo serviceConnectInfo = start.config.getServiceConnectInfo();
        System.out.println(serviceConnectInfo);
    }
}
