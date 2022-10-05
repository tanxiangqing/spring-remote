/**
 * Lakala.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package org.storevm.framework.remote.httpclient;

/**
 * 
 * @author Administrator
 * @version $Id: AuthSSLInitializationError.java, v 0.1 2015��7��27�� ����1:04:25 Administrator Exp $
 */
public class HttpsInitializationError extends Error {
    /**
     * UID
     */
    private static final long serialVersionUID = -4760245611892781295L;

    /**
     * 
     */
    public HttpsInitializationError() {
        super();
    }

    /**
     * 
     * @param message
     */
    public HttpsInitializationError(String message) {
        super(message);
    }
}
