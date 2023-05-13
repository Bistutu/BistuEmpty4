package com.thinkstu.config;

import com.thinkstu.utils.*;
import okhttp3.*;
import org.springframework.context.annotation.*;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

/**
 * @author : ThinkStu
 * @since : 2023/4/3, 17:35, 周一
 **/
@Configuration
public class OkhttpConfig {
    @Bean
    OkHttpClient okHttpClient() {
        try {
            // 作用：解决教务网的SSL证书问题，_.bistu.edu.cn.cer 证书源自教务网，手动下载即可
            CertificateFactory cf   = CertificateFactory.getInstance("X.509");
            String             path = System.getProperty("user.dir") + "/_.bistu.edu.cn.cer";
            try (InputStream caInput = new BufferedInputStream(new FileInputStream(path))) {
                X509Certificate ca           = (X509Certificate) cf.generateCertificate(caInput);
                String          keyStoreType = KeyStore.getDefaultType();
                KeyStore        keyStore     = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);
                // 创建一个信任管理器，信任我们密钥库中的CA
                String              tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf          = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);
                // 创建一个使用我们的 TrustManager 的 SSLContext
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, tmf.getTrustManagers(), null);
                return new OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0])
                        .addInterceptor(new RetryInterceptor(3, 1000))
                        .build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
