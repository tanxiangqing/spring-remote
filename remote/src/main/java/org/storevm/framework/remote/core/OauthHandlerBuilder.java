package org.storevm.framework.remote.core;

import org.storevm.framework.remote.config.OauthConfig;
import org.storevm.framework.remote.enums.GrantType;
import org.storevm.framework.remote.handler.OauthHandler;
import org.storevm.framework.remote.handler.OauthRefreshTokenHandler;
import org.storevm.framework.remote.handler.OauthRegisterHandler;
import org.storevm.framework.remote.handler.OauthTokenHandler;

public class OauthHandlerBuilder {
    private OauthConfig config;

    public OauthHandlerBuilder(OauthConfig config) {
        this.config = config;
    }

    public OauthHandler getHandler(GrantType type) {
        if (type == GrantType.AUTH_CODE) {
            return new OauthRegisterHandler(this.config);
        } else if (type == GrantType.CLIENT_CREDENTIALS) {
            return new OauthTokenHandler(this.config);
        } else if (type == GrantType.REFRESH_TOKEN) {
            return new OauthRefreshTokenHandler(this.config);
        }
        return null;
    }
}
