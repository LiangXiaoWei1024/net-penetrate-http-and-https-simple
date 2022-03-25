package com.kele.penetrate;

import com.kele.penetrate.factory.Autowired;
import com.kele.penetrate.factory.BeanFactoryImpl;
import com.kele.penetrate.factory.Recognizer;
import com.kele.penetrate.receiver.http.NettyHttpService;
import com.kele.penetrate.receiver.https.NettyHttpsService;
import com.kele.penetrate.service.NettyServiceInit;


@SuppressWarnings("unused")
@Recognizer
public class Start
{
    private static final BeanFactoryImpl beanFactory = new BeanFactoryImpl();

    @Autowired
    private NettyHttpService nettyHttpService;
    @Autowired
    private NettyHttpsService nettyHttpsService;
    @Autowired
    private NettyServiceInit nettyServiceInit;

    public static void main(String[] args)
    {
        Start start = beanFactory.getBean(Start.class);
        start.nettyHttpService.start();
        start.nettyHttpsService.start();
        start.nettyServiceInit.start();

        //<editor-fold desc="心跳">
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask()
//        {
//            @SneakyThrows
//            public void run()
//            {
//                //<editor-fold desc="心跳">
//                JSONObject heartbeat = new JSONObject();
//                heartbeat.put("ping", System.currentTimeMillis());
//                replyAll(heartbeat.toJSONString());
//                //</editor-fold>
//
//            }
//        }, 1000 * 5, 1000 * 5);
        //</editor-fold>
    }
}
