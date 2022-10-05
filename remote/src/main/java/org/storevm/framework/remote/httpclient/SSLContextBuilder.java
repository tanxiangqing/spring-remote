/*
 * @(#)SSLContextBuilder.java 2019/09/17
 *
 * Copyright (c) 2004-2019 Lakala, Inc. Wuxing Road, building 3, Lane 727, Shanghai, China All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Lakala, Inc. You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into
 * with Lakala.
 */
package org.storevm.framework.remote.httpclient;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.storevm.framework.remote.config.SslClientConfig;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * @author Jack
 * @version 1.0.0
 * @date 2019/09/17
 */
public class SSLContextBuilder {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SSLContextBuilder.class);

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
            // 如果密钥文件不为NULL且存在则创建密钥管理器对象
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
            logger.error("创建SSL上下文时发生异常", e);
            throw new HttpsInitializationError("Unsupported algorithm exception: " + e.getMessage());
        } catch (KeyStoreException e) {
            logger.error("创建SSL上下文时发生异常", e);
            throw new HttpsInitializationError("Keystore exception: " + e.getMessage());
        } catch (GeneralSecurityException e) {
            logger.error("创建SSL上下文时发生异常", e);
            throw new HttpsInitializationError("Key management exception: " + e.getMessage());
        }
    }

    /**
     * 创建密钥管理器对象
     *
     * @param keystore 密钥库文件
     * @param password 密钥库密码
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     */
    private KeyManager[] createKeyManagers(final KeyStore keystore, final String password)
            throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, password != null ? password.toCharArray() : null);
        return kmfactory.getKeyManagers();
    }

    /**
     * 创建信任证书库管理器对象数组
     *
     * @param keystore 信任证书库
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     */
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

    /**
     * 创建密钥库对象
     *
     * @param file
     * @param password
     * @param type
     * @return
     */
    private KeyStore createKeyStore(final File file, final String password, String type) {
        try (InputStream is = FileUtils.openInputStream(file)) {
            logger.debug("file:{}, password:{}, type:{}", file, password, type);
            KeyStore keystore = KeyStore.getInstance(type);
            keystore.load(is, password != null ? password.toCharArray() : null);
            return keystore;
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException ex) {
            logger.error("加载秘钥库时发生异常, file={}", ex, file);
        }
        return (KeyStore) null;
    }
}
