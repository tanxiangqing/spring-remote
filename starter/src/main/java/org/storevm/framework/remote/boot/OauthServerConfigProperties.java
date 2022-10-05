package org.storevm.framework.remote.boot;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OauthServerConfigProperties implements Serializable {
    String resourceId;
    String host;
    String clientSecret;
    String token;
}
