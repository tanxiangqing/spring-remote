package org.storevm.framework.remote.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OauthServerConfig implements Serializable {
    String resourceId;
    String host;
    String clientSecret;
    String token;
}
