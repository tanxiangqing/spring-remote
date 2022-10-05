package org.storevm.framework.remote.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OauthClientConfig implements Serializable {
    /**
     * 是否启用OAuth2客户端配置
     */
    boolean enabled;
    String clientId;
    OauthServerConfig[] authServers;
}
