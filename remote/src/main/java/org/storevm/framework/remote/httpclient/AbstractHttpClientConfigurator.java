package org.storevm.framework.remote.httpclient;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Jack
 */
@Slf4j
public abstract class AbstractHttpClientConfigurator implements InitializingBean {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    @Getter
    protected CloseableHttpClient httpClient;

    @Getter
    protected RequestConfig config;

    protected HttpClientConfig properties;

    @Setter
    @Getter
    protected OauthConfig oauthConfig;

    public AbstractHttpClientConfigurator(HttpClientConfig properties) {
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
        httpClientConnectionManager.setMaxTotal(properties.getMaxTotal());
        httpClientConnectionManager.setDefaultMaxPerRoute(properties.getDefaultMaxPerRoute());
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
