package org.storevm.framework.remote.httpclient;

import org.storevm.framework.remote.config.HttpClientConfig;

import javax.net.ssl.SSLContext;

/**
 * @author Jack
 */
public class HttpsClientConfigurator extends HttpClientConfigurator {
    public HttpsClientConfigurator(HttpClientConfig properties) {
        super(properties);
    }

    @Override
    protected SSLContext buildSSLContext() throws Exception {
        if (properties.getSsl() != null && properties.getSsl().isEnabled()) {
            SSLContextBuilder builder = new SSLContextBuilder(properties.getSsl());
            return builder.build();
        }
        return super.buildSSLContext();
    }
}
