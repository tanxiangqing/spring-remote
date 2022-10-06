package org.storevm.framework.remote.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.storevm.framework.remote.core.CallResult;
import org.storevm.framework.remote.httpclient.HttpClientConfigurator;

import java.net.URI;
import java.util.List;

@Slf4j
public class PostHttpInvokeHandler extends AbstractHttpInvokeHandler {
    public PostHttpInvokeHandler(HttpClientConfigurator template) {
        super(template);
    }

    @Override
    public CallResult invoke(URI uri, List<Header> headers, HttpEntity entity) {
        HttpPost post = new HttpPost(uri);
        if (entity != null) {
            post.setEntity(entity);
        }
        return doInvoke(post, headers);
    }
}
