package org.storevm.framework.remote.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.storevm.framework.remote.core.CallResult;
import org.storevm.framework.remote.httpclient.HttpClientConfigurator;

import java.net.URI;
import java.util.List;

@Slf4j
public class GetHttpInvokeHandler extends AbstractHttpInvokeHandler {
    /**
     * constructor
     *
     * @param template
     */
    public GetHttpInvokeHandler(HttpClientConfigurator template) {
        super(template);
    }

    @Override
    public CallResult invoke(URI uri, List<Header> headers, HttpEntity entity) {
        HttpGet get = new HttpGet(uri);
        return doInvoke(get, headers);
    }
}
