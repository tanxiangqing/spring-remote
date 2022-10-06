package org.storevm.framework.remote.httpclient;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Jack
 */
public class HttpsX509TrustManager implements X509TrustManager {
    private X509TrustManager defaultTrustManager;

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
