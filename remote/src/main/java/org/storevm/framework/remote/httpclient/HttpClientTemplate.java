/*
 * @(#)HttpClientTemplateImpl.java 2019/09/16
 *
 * Copyright (c) 2004-2019 Lakala, Inc. Wuxing Road, building 3, Lane 727, Shanghai, China All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Lakala, Inc. You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into
 * with Lakala.
 */
package org.storevm.framework.remote.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.storevm.framework.remote.config.HttpClientConfig;

import java.io.IOException;
import java.net.URI;

/**
 * @author Jack
 * @version 1.0.0
 * @date 2019/09/16
 */
public class HttpClientTemplate extends AbstractHttpClientTemplate {
    /**
     * constructor
     *
     * @param properties
     */
    public HttpClientTemplate(HttpClientConfig properties) {
        super(properties);
    }

}
