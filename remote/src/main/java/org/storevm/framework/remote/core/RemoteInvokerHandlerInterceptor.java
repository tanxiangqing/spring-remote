package org.storevm.framework.remote.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.storevm.framework.remote.annotation.EnabledOAuth;
import org.storevm.framework.remote.httpclient.HttpClientConfigurator;

import java.lang.reflect.Method;

@Slf4j
@Component
public class RemoteInvokerHandlerInterceptor implements MethodInterceptor {
    private final HttpClientConfigurator template;
    private final SpringBeanExpressionResolver resolver;

    @Autowired
    public RemoteInvokerHandlerInterceptor(SpringBeanExpressionResolver resolver, HttpClientConfigurator template) {
        this.resolver = resolver;
        this.template = template;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        RemoteInvokerBuilder builder = new RemoteInvokerBuilder().withTargetClass(method.getDeclaringClass())
                .withMethod(method).withValues(objects);
        RemoteInvoker invoker = builder.build();
        if (isOauthInvoker(method)) {
            invoker = new OauthRemoteInvoker(invoker);
        }
        invoker.resolve(this.resolver);
        try (CallResult result = invoker.invoke(this.template)) {
            if (result.isSuccess()) {
                return result.getReturnObject(method.getGenericReturnType());
            }
        }
        return null;
    }

    private boolean isOauthInvoker(Method method) {
        return AnnotationUtils.findAnnotation(method, EnabledOAuth.class) != null;
    }
}
