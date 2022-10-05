package org.storevm.framework.remote.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.storevm.framework.remote.core.OauthHandlerBuilder;
import org.storevm.framework.remote.core.OauthRequest;
import org.storevm.framework.remote.core.OauthToken;
import org.storevm.framework.remote.enums.GrantType;
import org.storevm.framework.remote.handler.OauthHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/oauth")
@ConditionalOnProperty(prefix = "oauth2", name = {"enabled"}, havingValue = "true")
public class OauthServiceController {
    private OauthHandlerBuilder builder;
    private OauthConfigProperties config;

    public OauthServiceController(OauthHandlerBuilder builder, OauthConfigProperties config) {
        this.builder = builder;
        this.config = config;
    }

    @PostMapping("register")
    public OauthToken register(@RequestParam("resource_id") String resource, @RequestParam("client_id") String client) {
        OauthRequest request = new OauthRequest();
        request.setAudience(client);
        request.setSubject(resource);
        OauthHandler handler = this.builder.getHandler(GrantType.AUTH_CODE);
        if (handler != null) {
            OauthToken token = handler.handle(request);
            return token;
        }
        return null;
    }

    @PostMapping("token")
    public OauthToken token(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "resource_id") String resourceId, @RequestParam("client_id") String clientId,
                            @RequestParam("client_secret") String clientSecret, @RequestParam("grant_type") String grantType,
                            @RequestParam(value = "redirect_uri", required = false) String redirectUri,
                            @RequestParam(value = "code", required = false) String code,
                            @RequestParam(value = "refresh_token", required = false) String refreshToken,
                            @RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "password", required = false) String password) {
        log.debug("obtain all of parameters, grant_type={},client_id={},client_secret={},redirect_uri={},code={},refresh_token={},username={},password={}}",
                grantType, clientId, clientSecret, redirectUri, code, refreshToken, username, password);
        GrantType type = GrantType.valuesOf(grantType);
        log.debug("obtain grant type is {}", type);
        OauthHandler handler = this.builder.getHandler(type);
        if (handler != null) {
            OauthRequest req = new OauthRequest();
            req.setAudience(clientId);
            req.setSecret(clientSecret);
            req.setSubject(resourceId);
            req.setToken(refreshToken);
            if (this.config.getTimeout() != null) {
                req.setExpired(this.config.getTimeout().getSeconds());
            }
            return handler.handle(req);
        }
        return null;
    }
}
