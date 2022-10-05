package org.storevm.framework.remote.core;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OauthRequest implements Serializable {
    String issuer;
    String audience;
    String subject;
    String secret;
    String token;
    long expired = 72 * 60 * 60;
    String host;
}
