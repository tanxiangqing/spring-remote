/*
 * @(#)AbstractHttpClientTemplate.java 2019/09/17
 *
 * Copyright (c) 2004-2019 Lakala, Inc. Wuxing Road, building 3, Lane 727, Shanghai, China All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Lakala, Inc. You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into
 * with Lakala.
 */
package org.storevm.framework.remote.httpclient;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.storevm.framework.remote.config.HttpClientConfig;
import org.storevm.framework.remote.config.OauthConfig;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Jack
 * @version 1.0.0
 * @date 2019/09/17
 */
@Slf4j
public abstract class AbstractHttpClientTemplate implements InitializingBean {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    /**
     * HTTP客户端
     */
    @Getter
    protected CloseableHttpClient httpClient;

    /**
     * 请求配置
     */
    @Getter
    protected RequestConfig config;

    /**
     * 配置属性
     */
    protected HttpClientConfig properties;

    /**
     * OAuth2配置
     */
    @Setter
    @Getter
    protected OauthConfig oauthConfig;

    /**
     * constructor
     *
     * @param properties
     */
    public AbstractHttpClientTemplate(HttpClientConfig properties) {
        this.properties = properties;
    }

    /**
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(buildSSLContext(),
                new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(HTTP, new PlainConnectionSocketFactory()).register(HTTPS, sslsf).build();
        PoolingHttpClientConnectionManager httpClientConnectionManager
                = new PoolingHttpClientConnectionManager(registry);
        // 最大连接数
        httpClientConnectionManager.setMaxTotal(properties.getMaxTotal());
        // 并发数
        httpClientConnectionManager.setDefaultMaxPerRoute(properties.getDefaultMaxPerRoute());
        // HttpClientBuilder中的构造方法被protected修饰，所以这里不能直接使用new来实例化一个HttpClientBuilder，可以使用HttpClientBuilder提供的静态方法create()来获取HttpClientBuilder对象
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(httpClientConnectionManager);
        httpClientBuilder.setConnectionManagerShared(true);
        httpClientBuilder.setSSLSocketFactory(sslsf);
        this.httpClient = httpClientBuilder.build();
        this.config = buildRequestConfig();
    }

    protected SSLContext buildSSLContext() throws Exception {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        });
        return builder.build();
    }

    private RequestConfig buildRequestConfig() {
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(properties.getConnectTimeout())
                .setConnectionRequestTimeout(properties.getConnectionRequestTimeout())
                .setSocketTimeout(properties.getSocketTimeout()).build();
    }
}
