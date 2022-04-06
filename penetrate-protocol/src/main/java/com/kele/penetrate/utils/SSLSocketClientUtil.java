package com.kele.penetrate.utils;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Slf4j
@SuppressWarnings("unused")
public class SSLSocketClientUtil
{
    public static SSLSocketFactory getSocketFactory(TrustManager manager) {
        SSLSocketFactory socketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{manager}, new SecureRandom());
            socketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return socketFactory;
    }

    public static X509TrustManager getX509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
            {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
            {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    public static HostnameVerifier getHostnameVerifier() {
        return (s, sslSession) -> true;
    }
}
