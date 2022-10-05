package org.storevm.framework.remote.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.storevm.framework.remote.config.OauthConfig;
import org.storevm.framework.remote.enums.GrantType;
import org.storevm.framework.remote.httpclient.HttpClientTemplate;
import org.storevm.framework.remote.utils.JsonUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class OauthTokenClient {
    private OauthConfig config;
    private HttpClientTemplate template;

    public OauthTokenClient(HttpClientTemplate template) {
        this.template = template;
        this.config = this.template.getOauthConfig();
    }

    public OauthToken refresh(OauthRequest request) {
        try {
            URI uri = new URI(request.getHost());
            URIBuilder builder = new URIBuilder(uri);
            builder.setPath("/oauth/token");
            NameValuePair[] params = new BasicNameValuePair[]{
                    new BasicNameValuePair("resource_id", request.getSubject()),
                    new BasicNameValuePair("client_id", request.getAudience()),
                    new BasicNameValuePair("client_secret", request.getSecret()),
                    new BasicNameValuePair("grant_type", GrantType.REFRESH_TOKEN.getCode()),
                    new BasicNameValuePair("refresh_token", request.getToken())
            };
            builder.setParameters(params);
            HttpPost post = new HttpPost(builder.build());
            post.addHeader("Connection", "close");
            post.setConfig(this.template.getConfig());
            try (CloseableHttpResponse response = this.template.getHttpClient().execute(post)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    OauthToken token = JsonUtils.parse(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8), OauthToken.class);
                    return token;
                }
            }
        } catch (URISyntaxException | IOException ex) {
            log.error("occurred exception", ex);
        }
        return null;
    }
}
