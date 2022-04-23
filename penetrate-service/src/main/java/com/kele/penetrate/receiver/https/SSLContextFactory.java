package com.kele.penetrate.receiver.https;

import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.utils.FileUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
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

    public SSLContext getSslContext() throws Exception
    {
        char[] passArray = config.getMySSL().getPassword().toCharArray();
        SSLContext sslContext = SSLContext.getInstance("TLSv1");
        KeyStore ks = KeyStore.getInstance("JKS");
        //加载keytool 生成的文件
        FileInputStream inputStream = new FileInputStream(fileUtils.rootDirectory + "/" + config.getMySSL().getName());
        ks.load(inputStream, passArray);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, passArray);
        sslContext.init(kmf.getKeyManagers(), null, null);
        inputStream.close();
        return sslContext;
    }

}
