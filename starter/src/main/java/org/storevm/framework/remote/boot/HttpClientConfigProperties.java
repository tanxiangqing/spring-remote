/*
 * @(#)HttpClientConfigProperties.java 2019/09/16
 *
 * Copyright (c) 2004-2019 Lakala, Inc. Wuxing Road, building 3, Lane 727, Shanghai, China All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Lakala, Inc. You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into
 * with Lakala.
 */
package org.storevm.framework.remote.boot;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author Jack
 * @version 1.0.0
 * @date 2019/09/16
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties("http")
public class HttpClientConfigProperties {
    /**
     * 是否启用HttpClient服务
     */
    boolean enabled;

    /**
     * HTTP客户端连接池的最大连接数
     */
    Integer maxTotal = 10;

    /**
     * HTTP客户端连接的最大并发数
     */
    Integer defaultMaxPerRoute = 10;

    /**
     * 客户端和服务器建立连接的超时
     */
    Integer connectTimeout = 5000;

    /**
     * 连接池获取连接的超时
     */
    Integer connectionRequestTimeout = 500;

    /**
     * 客户端从服务器读取数据的超时
     */
    Integer socketTimeout = 5000;

    /**
     * 需要解压缩请求体的过滤器路径
     */
    String[] compressUrlPatterns = new String[]{"/*"};

    /**
     * 扫描代理接口类的包名
     */
    String scanPackages;

    /**
     * https相关配置
     */
    @NestedConfigurationProperty
    HttpClientSSLConfigProperties ssl;
}
