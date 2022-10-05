package org.storevm.framework.remote.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.storevm.framework.remote.annotation.EnabledOAuth;
import org.storevm.framework.remote.core.OauthToken;
import org.storevm.framework.remote.core.SpringBeanExpressionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class OauthHandlerInterceptor implements HandlerInterceptor {
    private SpringBeanExpressionResolver resolver;

    public OauthHandlerInterceptor(SpringBeanExpressionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            EnabledOAuth annotation = AnnotationUtils.findAnnotation(hm.getMethod(), EnabledOAuth.class);
            if (annotation != null) {
                String issuer = String.valueOf(resolver.resolveExpression(annotation.value()));
                String token = request.getHeader("Authorization");
                if (token == null) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "403 Forbidden");
                    return false;
                } else {
                    OauthToken ot = OauthToken.parse(StringUtils.removeStartIgnoreCase(token, "Bearer "));
                    if (!ot.verify(issuer)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "403 Forbidden");
                        return false;
                    }
                }
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
