package org.storevm.framework.remote.boot;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.storevm.framework.remote.interceptor.OauthHandlerInterceptor;

@Configuration
public class OauthHandlerInterceptorConfigurer implements WebMvcConfigurer {
    private OauthHandlerInterceptor oauthHandlerInterceptor;

    public OauthHandlerInterceptorConfigurer(OauthHandlerInterceptor oauthHandlerInterceptor) {
        this.oauthHandlerInterceptor = oauthHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.oauthHandlerInterceptor);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
