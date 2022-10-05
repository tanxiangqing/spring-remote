package org.storevm.framework.remote.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.UUID;

@Slf4j
public class OauthTokenBuilder {
    private OauthRequest request;

    public OauthTokenBuilder withRequest(OauthRequest request) {
        this.request = request;
        return this;
    }

    public OauthToken build() {
        Date today = new Date();
        Date expired = DateUtils.addSeconds(today, (int) request.getExpired());
        OauthToken token = new OauthToken();
        String secret = StringUtils.replace(UUID.randomUUID().toString(), "-", "").toUpperCase();
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String value = JWT.create().withIssuer(this.request.getIssuer()).withSubject(request.getSubject())
                .withAudience(request.getAudience()).withIssuedAt(today).withExpiresAt(expired).sign(algorithm);
        token.setToken(value);
        token.setSecret(secret);
        token.setRefreshToken(request.getToken());
        return token;
    }
}
