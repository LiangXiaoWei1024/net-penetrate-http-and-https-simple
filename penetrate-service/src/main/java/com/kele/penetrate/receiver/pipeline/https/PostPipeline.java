package com.kele.penetrate.receiver.pipeline.https;

import com.kele.penetrate.enumeration.RequestContentType;
import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.annotation.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.http.AnalysisHttpPostRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.*;

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
        if (AnalysisHttpPostRequest.getRequestType(fullHttpRequest) == RequestType.POST)
        {
            System.out.println("进入http post");
            Map<String, String> requestHeaders = AnalysisHttpPostRequest.getRequestHeaders(fullHttpRequest);
            String homeUser = AnalysisHttpPostRequest.getHomeUser(fullHttpRequest);
            String contentType = fullHttpRequest.headers().get("Content-Type");
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


            return true;
        }
        return false;
    }
}
