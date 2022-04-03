package com.kele.penetrate.receiver.http;

import com.alibaba.fastjson.JSONObject;
import com.kele.penetrate.Start;
import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.pojo.PipelineTransmission;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    /*
     * 处理请求
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws IOException
    {
        Start.httpEvents.notice(new PipelineTransmission(channelHandlerContext,fullHttpRequest));
//        System.out.println(fullHttpRequest);

//        HttpDataFactory factory = new DefaultHttpDataFactory(true);
//        HttpPostRequestDecoder httpDecoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
//        httpDecoder.setDiscardThreshold(0);
//        final HttpContent chunk = fullHttpRequest;
//        httpDecoder.offer(chunk);
//        if (chunk instanceof LastHttpContent)
//        {
//            List<InterfaceHttpData> interfaceHttpDataList = httpDecoder.getBodyHttpDatas();
//            for (InterfaceHttpData data : interfaceHttpDataList)
//            {
//                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload)
//                {
//                    FileUpload fileUpload = (FileUpload) data;
//                    try (FileOutputStream fileOutputStream = new FileOutputStream("netty_pic.png"))
//                    {
//                        fileOutputStream.write(fileUpload.get());
//                        fileOutputStream.flush();
//                    }
//                    catch (IOException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//                //如果数据类型为参数类型，则保存到body对象中
//                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
//                {
//                    Attribute attribute = (Attribute) data;
//                    System.out.println(attribute.getName() + ":" + attribute.getValue());
//                }
//            }
//        }
//
//
//        FullHttpResponse response;
//        if (fullHttpRequest.method() == HttpMethod.GET)
//        {
//            Map<String, Object> getParamsFromChannel = getGetParamsFromChannel(fullHttpRequest);
//            System.out.println(getParamsFromChannel);
//            String data = "GET method over";
//            ByteBuf buf = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
//            response = responseOK(HttpResponseStatus.OK, buf);
//
//        }
//        else if (fullHttpRequest.method() == HttpMethod.POST)
//        {
//            System.out.println(getPostParamsFromChannel(fullHttpRequest));
//            String data = "POST method over";
//            ByteBuf content = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
//            response = responseOK(HttpResponseStatus.OK, content);
//
//        }
//        else
//        {
//            response = responseOK(HttpResponseStatus.INTERNAL_SERVER_ERROR, null);
//        }
        // 发送响应
//        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    /*
     * 获取GET方式传递的参数
     */
    private Map<String, Object> getGetParamsFromChannel(FullHttpRequest fullHttpRequest)
    {

        Map<String, Object> params = new HashMap<>();

        if (fullHttpRequest.method() == HttpMethod.GET)
        {
            // 处理get请求
            QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
            Map<String, List<String>> paramList = decoder.parameters();
            for (Map.Entry<String, List<String>> entry : paramList.entrySet())
            {
                params.put(entry.getKey(), entry.getValue().get(0));
                System.out.println("========" + entry.getKey() + ":" + entry.getValue().get(0));
            }
            return params;
        }
        else
        {
            return null;
        }

    }

    /*
     * 获取POST方式传递的参数
     */
    private Map<String, Object> getPostParamsFromChannel(FullHttpRequest fullHttpRequest)
    {

        Map<String, Object> params = new HashMap<String, Object>();

        if (fullHttpRequest.method() == HttpMethod.POST)
        {
            // 处理POST请求
            String strContentType = fullHttpRequest.headers().get("Content-Type").trim();
            if (strContentType.contains("x-www-form-urlencoded"))
            {
                params = getFormParams(fullHttpRequest);
            }
            else if (strContentType.contains("application/json"))
            {
                try
                {
                    params = getJSONParams(fullHttpRequest);
                }
                catch (UnsupportedEncodingException e)
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
            return params;
        }
        else
        {
            return null;
        }
    }

    /*
     * 解析from表单数据（Content-Type = x-www-form-urlencoded）
     */
    private Map<String, Object> getFormParams(FullHttpRequest fullHttpRequest)
    {
        Map<String, Object> params = new HashMap<String, Object>();

        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();

        for (InterfaceHttpData data : postData)
        {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
            {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }

        return params;
    }

    /*
     * 解析json数据（Content-Type = application/json）
     */
    private Map<String, Object> getJSONParams(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException
    {
        Map<String, Object> params = new HashMap<String, Object>();

        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        String strContent = new String(reqContent, "UTF-8");

        JSONObject jsonParams = JSONObject.parseObject(strContent);
        for (Object key : jsonParams.keySet())
        {
            params.put(key.toString(), jsonParams.get(key));
        }

        return params;
    }

    private FullHttpResponse responseOK(HttpResponseStatus status, ByteBuf content)
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        if (content != null)
        {
            response.headers().set("Content-Type", "text/plain;charset=UTF-8");
            response.headers().set("Content_Length", response.content().readableBytes());
        }
        return response;
    }
}
