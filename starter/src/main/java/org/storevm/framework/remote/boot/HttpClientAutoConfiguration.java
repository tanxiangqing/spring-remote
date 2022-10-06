package org.storevm.framework.remote.boot;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.storevm.framework.remote.config.HttpClientConfig;
import org.storevm.framework.remote.config.SslClientConfig;
import org.storevm.framework.remote.core.SpringBeanExpressionResolver;
import org.storevm.framework.remote.filter.HttpServletCompressFilter;
import org.storevm.framework.remote.httpclient.HttpClientConfigurator;
import org.storevm.framework.remote.httpclient.HttpsClientConfigurator;

import java.util.Arrays;

/**
 * @author Jack
 */
@Configuration
@EnableConfigurationProperties(HttpClientConfigProperties.class)
@ComponentScan({"org.storevm.framework"})
@ConditionalOnProperty(prefix = "http", name = {"enabled"}, havingValue = "true")
public class HttpClientAutoConfiguration {

    /**
     * 配置属性
     */
    private HttpClientConfigProperties httpClientConfigProperties;

    /**
     * constructor
     */
    @Autowired
    public HttpClientAutoConfiguration(HttpClientConfigProperties httpClientConfigProperties) {
        this.httpClientConfigProperties = httpClientConfigProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpClientConfigurator httpClientConfigurator(HttpClientConfig config) {
        return new HttpsClientConfigurator(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringBeanExpressionResolver springBeanExpressionResolver() {
        return new SpringBeanExpressionResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpClientConfig HttpClientConfig() {
        return toHttpClientConfig(this.httpClientConfigProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean<HttpServletCompressFilter> registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new HttpServletCompressFilter());
        registrationBean.setUrlPatterns(Arrays.asList(httpClientConfigProperties.getCompressUrlPatterns()));
        return registrationBean;
    }

    private HttpClientConfig toHttpClientConfig(HttpClientConfigProperties properties) {
        HttpClientConfig config = new HttpClientConfig();
        BeanUtils.copyProperties(properties, config);
        if (properties.getSsl() != null) {
            SslClientConfig ssl = new SslClientConfig();
            BeanUtils.copyProperties(properties.getSsl(), ssl);
            config.setSsl(ssl);
        }
        return config;
    }
}
