package org.storevm.framework.remote.handler;

import org.storevm.framework.remote.core.OauthRequest;
import org.storevm.framework.remote.core.OauthToken;

public interface OauthHandler {
    OauthToken handle(OauthRequest request);
}
