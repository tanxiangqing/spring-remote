/*
 * @(#)HttpsClientTemplate.java 2019/09/17
 *
 * Copyright (c) 2004-2019 Lakala, Inc. Wuxing Road, building 3, Lane 727, Shanghai, China All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Lakala, Inc. You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into
 * with Lakala.
 */
package org.storevm.framework.remote.httpclient;

import org.storevm.framework.remote.config.HttpClientConfig;

import javax.net.ssl.SSLContext;

/**
 * 双向认证的HTTP客户端
 *
 * @author Jack
 * @version 1.0.0
 * @date 2019/09/17
 */
public class HttpsClientTemplate extends HttpClientTemplate {
    /**
     * constructor
     *
     * @param properties
     */
    public HttpsClientTemplate(HttpClientConfig properties) {
        super(properties);
    }

    @Override
    protected SSLContext buildSSLContext() throws Exception {
        if (properties.getSsl() != null && properties.getSsl().isEnabled()) {
            // 启用了双向认证
            SSLContextBuilder builder = new SSLContextBuilder(properties.getSsl());
            return builder.build();
        }
        return super.buildSSLContext();
    }

    /**
     public static void main(String[] args) throws Exception {
     SslClientConfig sslConfig = new SslClientConfig();
     sslConfig.setEnabled(true);
     sslConfig.setKeystoreFile(new File("D:\\projects\\lakala\\smartpos\\doc\\certs\\dev\\t_client.p12"));
     sslConfig.setKeystorePassword("123456");
     sslConfig.setTrustKeystoreFile(new File("D:\\projects\\lakala\\smartpos\\doc\\certs\\dev\\server.keystore"));
     sslConfig.setTrustKeystorePassword("123456");
     HttpClientConfig properties = new HttpClientConfig();
     properties.setEnabled(true);
     properties.setMaxTotal(200);
     properties.setDefaultMaxPerRoute(20);
     properties.setConnectTimeout(1000);
     properties.setConnectionRequestTimeout(500);
     properties.setSocketTimeout(10000);
     properties.setSsl(sslConfig);
     HttpsClientTemplate template = new HttpsClientTemplate(properties);
     NameValuePair[] pairs
     = {new BasicNameValuePair("termid", "YP610000208589"), new BasicNameValuePair("sign", "false")};
     Header[] headers = {new BasicHeader("Content-type", "text/html;charset=UTF-8")};
     String uri = "https://10.177.85.30:6941/client/silenceApp.do";
     }
     */
}
