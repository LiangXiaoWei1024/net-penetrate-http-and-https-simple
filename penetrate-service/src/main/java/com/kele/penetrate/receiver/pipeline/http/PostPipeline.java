package com.kele.penetrate.receiver.pipeline.http;

import com.kele.penetrate.enumeration.RequestContentType;
import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.utils.http.AnalysisHttpsRequest;
import com.kele.penetrate.utils.Func;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Register
@SuppressWarnings("unused")
public class PostPipeline implements Func<PipelineTransmission, Boolean>
{

    @Override
    public Boolean func(PipelineTransmission pipelineTransmission)
    {
        FullHttpRequest fullHttpRequest = pipelineTransmission.getFullHttpRequest();
        ChannelHandlerContext channelHandlerContext = pipelineTransmission.getChannelHandlerContext();
        if (AnalysisHttpsRequest.getRequestType(fullHttpRequest) == RequestType.POST)
        {
            System.out.println("进入http post");
            Map<String, String> requestHeaders = AnalysisHttpsRequest.getRequestHeaders(fullHttpRequest);
            String homeUser = AnalysisHttpsRequest.getHomeUser(fullHttpRequest);
            String contentType = fullHttpRequest.headers().get("Content-Type");
            System.out.println(contentType);
            //<editor-fold desc="处理 x-www-form-urlencoded">
            if (contentType.contains(RequestContentType.X_WWW_FORM_URLENCODED.getCode()))
            {
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
                List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();

                for (InterfaceHttpData data : postData)
                {
                    if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
                    {
                        MemoryAttribute attribute = (MemoryAttribute) data;
                        System.out.println(attribute);
                    }
                }

            }
            //</editor-fold>

            //<editor-fold desc="处理 multipart/form-data">
            if (contentType.contains(RequestContentType.MULTIPART_FORM_DATA.getCode()))
            {
                HttpDataFactory factory = new DefaultHttpDataFactory(true);
                HttpPostRequestDecoder httpDecoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
                httpDecoder.setDiscardThreshold(0);
                final HttpContent chunk = fullHttpRequest;
                httpDecoder.offer(chunk);
                if (chunk instanceof LastHttpContent)
                {
                    List<InterfaceHttpData> interfaceHttpDataList = httpDecoder.getBodyHttpDatas();
                    for (InterfaceHttpData data : interfaceHttpDataList)
                    {
                        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload)
                        {
                            FileUpload fileUpload = (FileUpload) data;
                            System.out.println(fileUpload.getFilename());
//                    try (FileOutputStream fileOutputStream = new FileOutputStream("netty_pic.png"))
//                    {
//                        fileOutputStream.write(fileUpload.get());
//                        fileOutputStream.flush();
//                    }
//                    catch (IOException e)
//                    {
//                        e.printStackTrace();
//                    }
                            System.out.println();
                        }
                        //如果数据类型为参数类型，则保存到body对象中
                        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
                        {
                            Attribute attribute = (Attribute) data;
                            System.out.println(attribute);
                        }
                    }
                }

            }
            //</editor-fold>

            //<editor-fold desc="处理 application(json xml javaScript),text(plain html)">
            if (contentType.contains(RequestContentType.APPLICATION_JSON.getCode()) ||
                    contentType.contains(RequestContentType.APPLICATION_JAVASCRIPT.getCode()) ||
                    contentType.contains(RequestContentType.APPLICATION_XML.getCode()) ||
                    contentType.contains(RequestContentType.TEXT_PLAIN.getCode())
            )
            {
                ByteBuf content = fullHttpRequest.content();
                byte[] reqContent = new byte[content.readableBytes()];
                content.readBytes(reqContent);
                String strContent = new String(reqContent, StandardCharsets.UTF_8);
                System.out.println(strContent);
            }
            //</editor-fold>
            return true;
        }
        return false;
    }
}
