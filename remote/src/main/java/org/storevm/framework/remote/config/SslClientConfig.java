package org.storevm.framework.remote.config;

import lombok.Data;

import java.io.File;

@Data
public class SslClientConfig {
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
