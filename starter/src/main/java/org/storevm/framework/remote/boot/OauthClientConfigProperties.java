package org.storevm.framework.remote.boot;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OauthClientConfigProperties implements Serializable {
    /**
     * 是否启用OAuth2客户端配置
     */
    boolean enabled;
    String clientId;
    @NestedConfigurationProperty
    OauthServerConfigProperties[] authServers;
}
