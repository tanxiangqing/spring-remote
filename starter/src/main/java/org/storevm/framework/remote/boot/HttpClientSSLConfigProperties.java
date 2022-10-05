/*
 * @(#)HttpClientSSLConfigProperties.java 2019/09/17
 *
 * Copyright (c) 2004-2019 Lakala, Inc. Wuxing Road, building 3, Lane 727, Shanghai, China All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Lakala, Inc. You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into
 * with Lakala.
 */
package org.storevm.framework.remote.boot;

import lombok.Data;

import java.io.File;

/**
 * HTTP客户端的SSL相关配置项
 *
 * @author Jack
 * @version 1.0.0
 * @date 2019/09/17
 */
@Data
public class HttpClientSSLConfigProperties {
    /**
     * 是否启用HTTPS双向认证
     */
    private boolean enabled;

    /**
     * 双向认证的私钥证书
     */
    private File keystoreFile;

    /**
     * 双向认证的信任证书库
     */
    private File trustKeystoreFile;

    /**
     * 双向认证的私钥证书密码
     */
    private String keystorePassword;

    /**
     * 双向认证的信任证书库密码
     */
    private String trustKeystorePassword;
}
