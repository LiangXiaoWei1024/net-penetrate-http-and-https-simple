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
    private static final BeanFactoryImpl beanFactory;
    public static final Events<PipelineTransmission> hypertextProtocolEvents;
    public static final Events<ServicePipeline> serviceEvents;
    private static final Start start;

    @Autowired
    private NettyHttpService nettyHttpService;
    @Autowired
    private NettyHttpsService nettyHttpsService;
    @Autowired
    private NettyServiceInit nettyServiceInit;
    @Autowired
    private ConnectManager connectManager;

    //<editor-fold desc="初始化">
    static
    {
        BeanFactoryImpl.setBean(new Timer());
        beanFactory = BeanFactoryImpl.getInstance("com.kele.penetrate");
        start = BeanFactoryImpl.getBean(Start.class);
        hypertextProtocolEvents = new Events<>("HTTP", PipelineTransmission.class, "com.kele.penetrate.receiver.pipeline");
        serviceEvents = new Events<>("Service", ServicePipeline.class, "com.kele.penetrate.service.pipeline");
    }
    //</editor-fold>

    //<editor-fold desc="心跳">
    public void heartbeat()
    {
        BeanFactoryImpl.getBean(Timer.class).schedule(new TimerTask()
        {
            @SneakyThrows
            public void run()
            {
                connectManager.replyAll(new Heartbeat());
            }
        }, 1000 * 20, 1000 * 20);
    }
    //</editor-fold>

    //<editor-fold desc="清理未处理的消息">
    public void clearUntreatedMsg()
    {
        BeanFactoryImpl.getBean(Timer.class).schedule(new TimerTask()
        {
            @SneakyThrows
            public void run()
            {
                connectManager.clearUntreatedMsg();
            }
        }, 1000 * 60, 1000 * 60);
    }
    //</editor-fold>

    public static void main(String[] args)
    {
        Start start = BeanFactoryImpl.getBean(Start.class);
        start.nettyHttpService.start();
        start.nettyHttpsService.start();
        start.nettyServiceInit.start();
        start.heartbeat();
        start.clearUntreatedMsg();
    }
}
