package org.storevm.framework.remote.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.storevm.framework.remote.core.CallResult;
import org.storevm.framework.remote.httpclient.HttpClientTemplate;

import java.net.URI;
import java.util.List;

@Slf4j
public class DeleteHttpInvokeHandler extends AbstractHttpInvokeHandler {
    public DeleteHttpInvokeHandler(HttpClientTemplate template) {
        super(template);
    }

    @Override
    public CallResult invoke(URI uri, List<Header> headers, HttpEntity entity) {
        HttpDelete delete = new HttpDelete(uri);
        return doInvoke(delete, headers);
    }
}
