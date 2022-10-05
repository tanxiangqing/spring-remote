package org.storevm.framework.remote.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.storevm.framework.remote.config.OauthConfig;
import org.storevm.framework.remote.core.OauthRequest;
import org.storevm.framework.remote.core.OauthToken;
import org.storevm.framework.remote.core.OauthTokenBuilder;

@Slf4j
public class OauthRefreshTokenHandler extends AbstractOauthHandler {
    public OauthRefreshTokenHandler(OauthConfig config) {
        super(config);
    }

    @Override
    OauthToken doHandle(OauthRequest request) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(request.getSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(request.getIssuer())
                    .withAudience(request.getAudience())
                    .withSubject(request.getSubject())
                    .build();
            DecodedJWT jwt = verifier.verify(request.getToken());
            OauthToken token = new OauthTokenBuilder().withRequest(request).build();
            return token;
        } catch (JWTVerificationException ex) {
            log.error("JWT Verification occurred exception", ex);
        }
        return null;
    }

}
