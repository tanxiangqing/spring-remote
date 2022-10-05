/*
 * @(#)HttpClientConfigProperties.java 2019/09/16
 *
 * Copyright (c) 2004-2019 Lakala, Inc. Wuxing Road, building 3, Lane 727, Shanghai, China All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Lakala, Inc. You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into
 * with Lakala.
 */
package org.storevm.framework.remote.config;

import lombok.Data;

/**
 * @author Jack
 * @version 1.0.0
 * @date 2019/09/16
 */
@Data
public class HttpClientConfig {
    /**
     * 是否启用HttpClient服务
     */
    private boolean enabled;

    /**
     * HTTP客户端连接池的最大连接数
     */
    private Integer maxTotal = 10;

    /**
     * HTTP客户端连接的最大并发数
     */
    private Integer defaultMaxPerRoute = 10;

    /**
     * 客户端和服务器建立连接的超时
     */
    private Integer connectTimeout = 5000;

    /**
     * 连接池获取连接的超时
     */
    private Integer connectionRequestTimeout = 500;

    /**
     * 客户端从服务器读取数据的超时
     */
    private Integer socketTimeout = 5000;

    /**
     * https相关配置
     */
    private SslClientConfig ssl;


    private OauthConfig oauth;
}
