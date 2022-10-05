package org.storevm.framework.remote.boot;

import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.storevm.framework.remote.config.OauthClientConfig;
import org.storevm.framework.remote.config.OauthConfig;
import org.storevm.framework.remote.config.OauthServerConfig;
import org.storevm.framework.remote.core.OauthHandlerBuilder;
import org.storevm.framework.remote.httpclient.HttpClientTemplate;

@Configuration
@EnableConfigurationProperties(OauthConfigProperties.class)
@ComponentScan({"org.storevm.framework"})
@ConditionalOnProperty(prefix = "oauth2", name = {"enabled"}, havingValue = "true")
public class Oauth2AutoConfiguration {
    private OauthConfigProperties config;

    /**
     * constructor
     *
     * @param config
     */
    public Oauth2AutoConfiguration(OauthConfigProperties config) {
        this.config = config;
    }

    @Bean
    @ConditionalOnMissingBean
    public OauthHandlerBuilder oauthHandlerBuilder(OauthConfig config) {
        return new OauthHandlerBuilder(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public OauthConfig oauthConfig(HttpClientTemplate template) {
        OauthConfig config = toOauthConfig();
        template.setOauthConfig(config);
        return config;
    }

    private OauthConfig toOauthConfig() {
        OauthConfig config = new OauthConfig();
        BeanUtils.copyProperties(this.config, config);
        if (this.config.getClient() != null) {
            OauthClientConfig client = new OauthClientConfig();
            BeanUtils.copyProperties(this.config.getClient(), client);
            config.setClient(client);
            if (this.config.getClient().getAuthServers() != null) {
                OauthServerConfig[] servers = new OauthServerConfig[this.config.getClient().getAuthServers().length];
                for (int i = 0, n = this.config.getClient().getAuthServers().length; i < n; i++) {
                    OauthServerConfig server = new OauthServerConfig();
                    BeanUtils.copyProperties(this.config.getClient().getAuthServers()[i], server);
                    servers[i] = server;
                }
                config.getClient().setAuthServers(servers);
            }
        }
        return config;
    }
}
