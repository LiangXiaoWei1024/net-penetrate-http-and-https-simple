package com.kele.penetrate;

import com.kele.penetrate.factory.BeanFactoryImpl;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.pojo.ServicePipeline;
import com.kele.penetrate.protocol.Heartbeat;
import com.kele.penetrate.receiver.http.NettyHttpService;
import com.kele.penetrate.receiver.https.NettyHttpsService;
import com.kele.penetrate.service.ConnectManager;
import com.kele.penetrate.service.NettyServiceInit;
import com.kele.penetrate.utils.Events;
import lombok.SneakyThrows;

import java.util.Timer;
import java.util.TimerTask;


@SuppressWarnings("unused")
@Recognizer
public class Start
{
    private static final BeanFactoryImpl beanFactory = new BeanFactoryImpl();
    public static final Events<PipelineTransmission> hypertextProtocolEvents = new Events("HTTP", PipelineTransmission.class, "com.kele.penetrate.receiver.pipeline");
    public static final Events<ServicePipeline> serviceEvents = new Events("Service", ServicePipeline.class, "com.kele.penetrate.service.pipeline");

    @Autowired
    private NettyHttpService nettyHttpService;
    @Autowired
    private NettyHttpsService nettyHttpsService;
    @Autowired
    private NettyServiceInit nettyServiceInit;
    @Autowired
    private ConnectManager connectManager;


    public static void main(String[] args)
    {
        Start start = beanFactory.getBean(Start.class);
        start.nettyHttpService.start();
        start.nettyHttpsService.start();
        start.nettyServiceInit.start();
        start.heartbeat();
        start.clearUntreatedMsg();
    }

    public void heartbeat()
    {
        //<editor-fold desc="心跳">
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @SneakyThrows
            public void run()
            {
                connectManager.replyAll(new Heartbeat());
            }
        }, 1000 * 10, 1000 * 10);
        //</editor-fold>
    }

    public void clearUntreatedMsg()
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @SneakyThrows
            public void run()
            {
                connectManager.clearUntreatedMsg();
            }
        }, 1000 * 60, 1000 * 60);
    }
}
