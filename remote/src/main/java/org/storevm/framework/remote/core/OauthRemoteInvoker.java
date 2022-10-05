package org.storevm.framework.remote.core;

import org.apache.http.message.BasicHeader;
import org.springframework.core.annotation.AnnotationUtils;
import org.storevm.framework.remote.annotation.EnabledOAuth;
import org.storevm.framework.remote.config.OauthServerConfig;
import org.storevm.framework.remote.httpclient.HttpClientTemplate;

import java.net.URISyntaxException;

/**
 * @author Jack
 */
public class OauthRemoteInvoker extends RemoteInvoker {
    private OauthToken token;
    private String resource;

    /**
     * constructor
     *
     * @param parent
     */
    public OauthRemoteInvoker(RemoteInvoker parent) {
        super(parent.getTargetClass(), parent.getMethod(), parent.getValues());
    }

    @Override
    public void resolve(SpringBeanExpressionResolver resolver) throws URISyntaxException {
        super.resolve(resolver);
        EnabledOAuth annotation = AnnotationUtils.findAnnotation(this.getMethod(), EnabledOAuth.class);
        if (annotation != null) {
            this.resource = String.valueOf(resolver.resolveExpression(annotation.value()));
            this.token = OauthTokenHolder.get(this.resource);
        }
    }

    @Override
    public CallResult invoke(HttpClientTemplate template) throws URISyntaxException {
        if (this.token == null || this.token.isTimeout()) {
            OauthRequest request = toOauthRequest(template);
            OauthTokenClient client = new OauthTokenClient(template);
            this.token = client.refresh(request);
            OauthTokenHolder.put(request.getSubject(), this.token);
        }
        if (this.token != null) {
            StringBuilder sb = new StringBuilder("Bearer ");
            sb.append(token.getToken()).append("@").append(token.getSecret());
            getHeaders().add(new BasicHeader("Authorization", sb.toString()));
        }
        return super.invoke(template);
    }

    private OauthRequest toOauthRequest(HttpClientTemplate template) {
        OauthRequest request = new OauthRequest();
        if (template.getOauthConfig() != null && template.getOauthConfig().getClient() != null) {
            request.setAudience(template.getOauthConfig().getClient().getClientId());
            OauthServerConfig config = template.getOauthConfig().getOauthServerConfig(this.resource);
            if (config != null) {
                request.setSubject(config.getResourceId());
                request.setSecret(config.getClientSecret());
                request.setToken(config.getToken());
                request.setHost(config.getHost());
            }
        }
        return request;
    }
}
