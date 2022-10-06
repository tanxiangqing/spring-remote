package org.storevm.framework.remote.handler;

import org.apache.http.HttpStatus;
import org.storevm.framework.remote.enums.CallMethod;
import org.storevm.framework.remote.exception.RemoteException;
import org.storevm.framework.remote.httpclient.HttpClientConfigurator;

public class HttpInvokeHandlerFactory {
    public static HttpInvokeHandler getHandler(HttpClientConfigurator template, CallMethod method) {
        if (method == CallMethod.POST) {
            return new PostHttpInvokeHandler(template);
        } else if (method == CallMethod.GET) {
            return new GetHttpInvokeHandler(template);
        } else if (method == CallMethod.PUT) {
            return new PutHttpInvokeHandler(template);
        } else if (method == CallMethod.DELETE) {
            return new DeleteHttpInvokeHandler(template);
        } else {
            throw new RemoteException(HttpStatus.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
        }
    }
}
