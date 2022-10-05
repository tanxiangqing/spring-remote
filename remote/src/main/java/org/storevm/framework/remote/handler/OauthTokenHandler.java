package org.storevm.framework.remote.handler;

import org.storevm.framework.remote.config.OauthConfig;
import org.storevm.framework.remote.core.OauthRequest;
import org.storevm.framework.remote.core.OauthToken;
import org.storevm.framework.remote.core.OauthTokenBuilder;

public class OauthTokenHandler extends AbstractOauthHandler {
    public OauthTokenHandler(OauthConfig config) {
        super(config);
    }

    @Override
    OauthToken doHandle(OauthRequest request) {
        OauthToken token = new OauthTokenBuilder().withRequest(request).build();
        return token;
    }
}
