package com.kele.penetrate;

import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.BeanFactoryImpl;
import com.kele.penetrate.factory.Recognizer;
import com.kele.penetrate.pojo.PipelineTransmission;
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
    public static final Events<PipelineTransmission> httpEvents = new Events("HTTP", PipelineTransmission.class, "com.kele.penetrate.receiver.pipeline.http");
    public static final Events<PipelineTransmission> httpsEvents = new Events("HTTPS", PipelineTransmission.class, "com.kele.penetrate.receiver.pipeline.https");
    public static final Events<Object> serviceEvents = new Events("Service", Object.class, "com.kele.penetrate.service.pipeline");

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
                Heartbeat heartbeat = new Heartbeat();
                connectManager.replyAll(heartbeat);
            }
        }, 1000 * 10, 1000 * 10);
        //</editor-fold>
    }
}
