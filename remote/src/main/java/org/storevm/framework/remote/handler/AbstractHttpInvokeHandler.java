package org.storevm.framework.remote.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.storevm.framework.remote.core.CallResult;
import org.storevm.framework.remote.exception.RemoteException;
import org.storevm.framework.remote.httpclient.HttpClientTemplate;

import java.io.IOException;
import java.util.List;

@Slf4j
public abstract class AbstractHttpInvokeHandler implements HttpInvokeHandler {
    protected HttpClientTemplate template;

    /**
     * constructor
     *
     * @param template
     */
    public AbstractHttpInvokeHandler(HttpClientTemplate template) {
        this.template = template;
    }

    protected CallResult doInvoke(HttpRequestBase request, List<Header> headers) {
        addHeaders(request, headers);
        request.setConfig(template.getConfig());
        try {
            CloseableHttpResponse response = template.getHttpClient().execute(request);
            return buildCallResult(response);
        } catch (IOException ex) {
            log.error("invoke httpclient to occur exception", ex);
            throw new RemoteException(HttpStatus.SC_INTERNAL_SERVER_ERROR, ex);
        }
    }

    protected CallResult buildCallResult(CloseableHttpResponse response) throws IOException {
        CallResult result = new CallResult(response);
        if (!result.isSuccess()) {
            throw new RemoteException(result.getStatusCode(), response.getStatusLine().getReasonPhrase());
        }
        return result;
    }

    protected void addHeaders(HttpRequestBase request, List<Header> headers) {
        request.addHeader("Connection", "close");
        if (headers != null && headers.size() > 0) {
            request.setHeaders(headers.toArray(headers.toArray(new Header[0])));
        }
    }

}
