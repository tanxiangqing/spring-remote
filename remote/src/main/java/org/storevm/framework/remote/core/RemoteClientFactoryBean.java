package org.storevm.framework.remote.core;

import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;

public class RemoteClientFactoryBean<T> implements FactoryBean<T> {
    @Setter
    private Class<T> origin;
    @Setter
    private RemoteInvokerHandlerInterceptor interceptor;

    @Autowired
    public RemoteClientFactoryBean(RemoteInvokerHandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public T getObject() throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(origin);
        enhancer.setCallback(this.interceptor);
        Object proxy = enhancer.create();
        return (T) proxy;
    }

    @Override
    public Class<T> getObjectType() {
        return origin;
    }
}
