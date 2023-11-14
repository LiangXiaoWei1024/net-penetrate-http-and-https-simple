package com.kele.penetrate.receiver.https;

import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.utils.FileUtils;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

@Recognizer
@SuppressWarnings("unused")
public class SSLContextFactory
{
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private Config config;

    public SslContext getSslContext() throws Exception
    {
        // 指定证书和私钥文件路径
        File certFile = new File(fileUtils.rootDirectory + "/" + config.getMySSL().getCert());
        File keyFile = new File(fileUtils.rootDirectory + "/" + config.getMySSL().getKey());

        // 配置SSL/TLS上下文
        SslContext sslContext = SslContextBuilder.forServer(certFile, keyFile, null)
                .sslProvider(SslContext.defaultServerProvider())
                .build();

        return sslContext;
    }

}
