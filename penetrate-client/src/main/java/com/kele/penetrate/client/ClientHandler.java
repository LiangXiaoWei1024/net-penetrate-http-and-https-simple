//package com.kele.penetrate.client;
//
//
//import com.alibaba.fastjson.JSONObject;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Map;
//
//@SuppressWarnings("unused")
//@Slf4j
//public class ClientHandler extends SimpleChannelInboundHandler<String>
//{
//    //<editor-fold desc="接收通道消息">
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, String msg)
//    {
//        JSONObject jsonObject = JSONObject.parseObject(msg);
//
//        //<editor-fold desc="心跳">
//        if (jsonObject.containsKey("ping"))
//        {
//            JSONObject pong = new JSONObject();
//            pong.put("pong", System.currentTimeMillis());
//            ctx.writeAndFlush(pong.toJSONString());
//            return;
//        }
//        //</editor-fold>
//
//        ConnectHandler connectHandlerByChannelId = com.bp.local.client.ConnectConfigure.getConnectHandlerByChannelId(ctx.channel().id());
//        if (connectHandlerByChannelId != null)
//        {
//            connectHandlerByChannelId.setLastReplyTime(System.currentTimeMillis());
//        }
//
//        //<editor-fold desc="数据处理">
//        if (jsonObject.containsKey("type") && jsonObject.containsKey("event"))
//        {
//            //<editor-fold desc="现货k线数据">
//            if ("spot".equals(jsonObject.getString("type")) && "kline".equals(jsonObject.getString("event")))
//            {
//                com.bp.local.client.DataFilterHandle.filterSpotKlineDataAndSend(jsonObject, () ->
//                        Start.queueHandler.append(jsonObject));
//
//                assert connectHandlerByChannelId != null;
//                Map<String, JSONObject> monitoringDataMap = connectHandlerByChannelId.getMonitoringDataMap();
//                String symbol = jsonObject.getString("s").toLowerCase();
//                if (!monitoringDataMap.containsKey(symbol))
//                {
//                    monitoringDataMap.put(symbol, new JSONObject());
//                }
//                JSONObject dataNode = monitoringDataMap.get(symbol);
//                dataNode.put("symbol", symbol);
//                dataNode.put("lastReplyTime", System.currentTimeMillis());
//                dataNode.put("lastReplyDataNodeTime", jsonObject.getLongValue("eventTime"));
//            }
//            //</editor-fold>
//        }
//        //</editor-fold>
//    }
//    //</editor-fold>
//
//    //<editor-fold desc="通道连接激活">
//    @Override
//    public void channelActive(ChannelHandlerContext ctx)
//    {
//    }
//    //</editor-fold>
//
//    //<editor-fold desc="通道断开">
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx)
//    {
//        disconnected(ctx);
//    }
//    //</editor-fold>
//
//    //<editor-fold desc="长时间没有通信">
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
//    {
//        ctx.flush();
//        ctx.close();
//    }
//    //</editor-fold>
//
//    //<editor-fold desc="连接断开处理">
//    private void disconnected(ChannelHandlerContext ctx)
//    {
//        ConnectHandler connectHandlerByChannelId = com.bp.local.client.ConnectConfigure.getConnectHandlerByChannelId(ctx.channel().id());
//        if (connectHandlerByChannelId != null)
//        {
//            log.info("与服务器连接断开：" + connectHandlerByChannelId.getIp() + ":" + connectHandlerByChannelId.getPort());
//            connectHandlerByChannelId.setChannel(null);
//            connectHandlerByChannelId.doConnect();
//        }
//    }
//    //</editor-fold>
//}
