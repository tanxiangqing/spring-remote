package org.storevm.framework.remote.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;
import org.storevm.framework.remote.core.CallResult;
import org.storevm.framework.remote.httpclient.HttpClientTemplate;

import java.net.URI;
import java.util.List;

@Slf4j
public class PutHttpInvokeHandler extends AbstractHttpInvokeHandler {
    public PutHttpInvokeHandler(HttpClientTemplate template) {
        super(template);
    }

    @Override
    public CallResult invoke(URI uri, List<Header> headers, HttpEntity entity) {
        HttpPut put = new HttpPut(uri);
        if (entity != null) {
            put.setEntity(entity);
        }
        return doInvoke(put, headers);
    }
}
