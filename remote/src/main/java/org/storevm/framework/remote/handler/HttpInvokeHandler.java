package org.storevm.framework.remote.handler;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.storevm.framework.remote.core.CallResult;

import java.net.URI;
import java.util.List;

/**
 * @author Jack
 */
public interface HttpInvokeHandler {
    CallResult invoke(URI uri, List<Header> headers, HttpEntity entity);
}
