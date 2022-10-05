package org.storevm.framework.remote.core;

import java.util.concurrent.ConcurrentHashMap;

public class OauthTokenHolder {
    private static ConcurrentHashMap<String, OauthToken> container = new ConcurrentHashMap<>(5);

    public static void put(String resource, OauthToken token) {
        container.put(resource, token);
    }

    public static OauthToken get(String resource) {
        return container.get(resource);
    }
}
