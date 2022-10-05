package org.storevm.framework.remote.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.convert.DurationUnit;

import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OauthConfig implements Serializable {
    /**
     * 是否启用OAuth2
     */
    boolean enabled;

    /**
     * 客户端配置
     */
    OauthClientConfig client;

    /**
     * 签发者
     */
    String issuer;

    /**
     * 过期时间（秒）
     */
    @DurationUnit(ChronoUnit.SECONDS)
    Duration timeout;

    public OauthServerConfig getOauthServerConfig(String resource) {
        if (client != null && client.getAuthServers() != null) {
            for (int i = 0, n = client.getAuthServers().length; i < n; i++) {
                if (StringUtils.equalsIgnoreCase(client.getAuthServers()[i].getResourceId(), resource)) {
                    return client.getAuthServers()[i];
                }
            }
        }
        return null;
    }
}
