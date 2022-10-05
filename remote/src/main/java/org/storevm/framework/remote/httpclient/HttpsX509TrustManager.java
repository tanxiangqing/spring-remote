/*
 * @(#)HttpsX509TrustManager.java 2019/05/17
 *
 * Copyright (c) 2004-2019 Lakala, Inc. Wuxing Road, building 3, Lane 727, Shanghai, China All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Lakala, Inc. You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into
 * with Lakala.
 */
package org.storevm.framework.remote.httpclient;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Jack
 * @date 2019/05/17
 * @version 1.0.0
 */
public class HttpsX509TrustManager implements X509TrustManager {
    private X509TrustManager defaultTrustManager;

    /**
     * Constructor for AuthSSLX509TrustManager.
     */
    public HttpsX509TrustManager(final X509TrustManager defaultTrustManager) {
        super();
        if (defaultTrustManager == null) {
            throw new IllegalArgumentException("Trust manager may not be null");
        }
        this.defaultTrustManager = defaultTrustManager;
    }

    /**
     * @see X509TrustManager#checkClientTrusted(X509Certificate[], String)
     */
    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        this.defaultTrustManager.checkClientTrusted(arg0, arg1);
    }

    /**
     * @see X509TrustManager#checkServerTrusted(X509Certificate[], String)
     */
    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        this.defaultTrustManager.checkServerTrusted(arg0, arg1);
    }

    /**
     * @see X509TrustManager#getAcceptedIssuers()
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.defaultTrustManager.getAcceptedIssuers();
    }

}
