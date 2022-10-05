package org.storevm.framework.remote.boot;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties("oauth2")
public class OauthConfigProperties {
    /**
     * 是否启用OAuth2
     */
    boolean enabled;

    /**
     * 签发者
     */
    String issuer;

    /**
     * 过期时间（秒）
     */
    @DurationUnit(ChronoUnit.SECONDS)
    Duration timeout;

    /**
     * OAuth2客户端配置
     */
    @NestedConfigurationProperty
    OauthClientConfigProperties client;
}
