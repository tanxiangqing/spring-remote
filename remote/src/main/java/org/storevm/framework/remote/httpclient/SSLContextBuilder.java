package org.storevm.framework.remote.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.storevm.framework.remote.config.SslClientConfig;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * @author Jack
 */
@Slf4j
public class SSLContextBuilder {
    /**
     * SSL配置项
     */
    private SslClientConfig config;

    /**
     * constructor
     *
     * @param config
     */
    public SSLContextBuilder(SslClientConfig config) {
        this.config = config;
    }

    /**
     * 创建SSL上下文对象
     *
     * @return SSLContext
     */
    public SSLContext build() {
        try {
            KeyManager[] keymanagers = null;
            TrustManager[] trustmanagers = null;
            if (config.getKeystoreFile() != null && config.getKeystoreFile().exists()) {
                KeyStore keystore = createKeyStore(config.getKeystoreFile(), config.getKeystorePassword(), "PKCS12");
                keymanagers = createKeyManagers(keystore, config.getKeystorePassword());
            }
            if (config.getTrustKeystoreFile() != null && config.getTrustKeystoreFile().exists()) {
                KeyStore keystore
                        = createKeyStore(config.getTrustKeystoreFile(), config.getTrustKeystorePassword(), "JKS");
                trustmanagers = createTrustManagers(keystore);
            }
            SSLContext sslcontext = SSLContext.getInstance("TLS"); // TLSv1
            sslcontext.init(keymanagers, trustmanagers, null);
            return sslcontext;
        } catch (NoSuchAlgorithmException e) {
            log.error("Unsupported algorithm exception", e);
            throw new HttpsInitializationError("Unsupported algorithm exception: " + e.getMessage());
        } catch (KeyStoreException e) {
            log.error("Keystore exception", e);
            throw new HttpsInitializationError("Keystore exception: " + e.getMessage());
        } catch (GeneralSecurityException e) {
            log.error("Key management exception", e);
            throw new HttpsInitializationError("Key management exception: " + e.getMessage());
        }
    }

    private KeyManager[] createKeyManagers(final KeyStore keystore, final String password)
            throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, password != null ? password.toCharArray() : null);
        return kmfactory.getKeyManagers();
    }

    private TrustManager[] createTrustManagers(final KeyStore keystore)
            throws KeyStoreException, NoSuchAlgorithmException {
        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmfactory.init(keystore);
        TrustManager[] trustmanagers = tmfactory.getTrustManagers();
        for (int i = 0; i < trustmanagers.length; i++) {
            if (trustmanagers[i] instanceof X509TrustManager) {
                trustmanagers[i] = new HttpsX509TrustManager((X509TrustManager) trustmanagers[i]);
            }
        }
        return trustmanagers;
    }

    private KeyStore createKeyStore(final File file, final String password, String type) {
        try (InputStream is = FileUtils.openInputStream(file)) {
            KeyStore keystore = KeyStore.getInstance(type);
            keystore.load(is, password != null ? password.toCharArray() : null);
            return keystore;
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException ex) {
            log.error("load keystore occurred exception", ex);
        }
        return (KeyStore) null;
    }
}
