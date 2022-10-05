package org.storevm.framework.remote.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author tanxiangqing
 */
@Slf4j
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OauthToken implements Serializable {
    @JsonProperty("access_token")
    private String token;
    @JsonProperty("token_type")
    private String type;
    @JsonProperty("expires_in")
    private Long expires;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("client_secret")
    private String secret;

    public boolean isTimeout() {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return false;
        } catch (TokenExpiredException ex) {
            log.warn("Oauth Token has already timeout.", ex);
        }
        return true;
    }

    public static OauthToken parse(String value) {
        OauthToken token = new OauthToken();
        if (StringUtils.isNotBlank(value)) {
            String[] values = StringUtils.split(value, "@");
            if (values != null && values.length > 1) {
                token.setToken(values[0]);
                token.setSecret(values[1]);
            }
        }
        return token;
    }

    public boolean verify(String issuer) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            log.warn("Oauth Token is incorrect.", ex);
        }
        return false;
    }
}
