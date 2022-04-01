package com.kele.penetrate.receiver.pipeline.http;

import com.kele.penetrate.enumeration.RequestType;
import com.kele.penetrate.factory.Register;
import com.kele.penetrate.pojo.PipelineTransmission;
import com.kele.penetrate.receiver.http.AnalysisHttpRequest;
import com.kele.penetrate.utils.Func;
import com.kele.penetrate.utils.PageTemplate;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.proxy.HttpProxyHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.util.Iterator;
import java.util.Map;

@Register
@SuppressWarnings("unused")
@Slf4j
public class GetPipeline implements Func<PipelineTransmission, Boolean>
{

    @Override
    public Boolean func(PipelineTransmission pipelineTransmission)
    {
        FullHttpRequest fullHttpRequest = pipelineTransmission.getFullHttpRequest();
        ChannelHandlerContext channelHandlerContext = pipelineTransmission.getChannelHandlerContext();

        if (AnalysisHttpRequest.getRequestType(fullHttpRequest) == RequestType.GET)
        {
            System.out.println("进入http get");
            HttpHeaders headers = fullHttpRequest.headers();
            String contentType = headers.get("Content-Type");

            Iterator<Map.Entry<String, String>> iterator = headers.iteratorAsString();
            while (iterator.hasNext())
            {
                Map.Entry<String, String> next = iterator.next();
                System.out.println(next);
            }

            if (contentType != null && contentType.contains("multipart/form-data"))
            {
                log.error("get 不支持上传文件");
                File file = new File("/Users/xiaoweiliang/Downloads/a.mp3");
                byte[] bytes = File2byte(file);
                ByteBuf content = Unpooled.copiedBuffer(bytes);


                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, content);
                channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
            else
            {
                File file = new File("/Users/xiaoweiliang/Downloads/a.mp3");
                byte[] bytes = File2byte(file);
                ByteBuf content = Unpooled.copiedBuffer(bytes);
                MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
                response.headers().set("Content-Type", "audio/mpeg");
                response.headers().set("Content_Length", response.content().readableBytes());
                response.headers().set("Accept-Ranges", bytes);
                channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }

            return true;
        }
        return false;
    }

    public static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }
}
