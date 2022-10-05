package org.storevm.framework.remote.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.storevm.framework.remote.config.OauthConfig;
import org.storevm.framework.remote.core.OauthRequest;
import org.storevm.framework.remote.core.OauthToken;

@Slf4j
public abstract class AbstractOauthHandler implements OauthHandler {
    protected OauthConfig config;

    public AbstractOauthHandler(OauthConfig config) {
        this.config = config;
    }

    @Override
    public OauthToken handle(OauthRequest request) {
        if (StringUtils.isNotBlank(this.config.getIssuer())) {
            request.setIssuer(this.config.getIssuer());
        }
        return doHandle(request);
    }

    abstract OauthToken doHandle(OauthRequest request);
}
