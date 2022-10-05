package org.storevm.framework.remote.core;

import java.lang.reflect.Method;

public class RemoteInvokerBuilder {
    private Class<?> targetClass;
    private Method method;
    private Object[] values;

    public RemoteInvokerBuilder withTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        return this;
    }

    public RemoteInvokerBuilder withMethod(Method method) {
        this.method = method;
        return this;
    }

    public RemoteInvokerBuilder withValues(Object[] values) {
        this.values = values;
        return this;
    }

    public RemoteInvoker build() {
        RemoteInvoker invoker = new RemoteInvoker(this.targetClass, this.method, this.values);
        return invoker;
    }
}
