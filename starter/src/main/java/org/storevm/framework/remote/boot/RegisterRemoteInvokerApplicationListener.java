package org.storevm.framework.remote.boot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.storevm.framework.remote.annotation.Remote;
import org.storevm.framework.remote.core.RemoteClientFactoryBean;
import org.storevm.framework.remote.utils.ClassUtils;

import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
public class RegisterRemoteInvokerApplicationListener implements ApplicationListener<ApplicationEvent> {
    private static final String HTTP_SCAN_PACKAGES_KEY = "http.scan-packages";
    private Set<Class<?>> classes;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            ApplicationEnvironmentPreparedEvent e = (ApplicationEnvironmentPreparedEvent) event;
            ConfigurableEnvironment environment = e.getEnvironment();
            String packages = environment.getProperty(HTTP_SCAN_PACKAGES_KEY);
            if (StringUtils.isNotBlank(packages)) {
                this.classes = getPackageClasses(StringUtils.split(packages, ","));
            }
        } else if (event instanceof ApplicationPreparedEvent) {
            ApplicationPreparedEvent e = (ApplicationPreparedEvent) event;
            ConfigurableApplicationContext context = e.getApplicationContext();
            if (classes != null && classes.size() > 0) {
                for (Class<?> type : classes) {
                    if (AnnotationUtils.findAnnotation(type, Remote.class) != null) {
                        registerBeanDefinition(type, context);
                    }
                }
            }
        }
    }

    private Set<Class<?>> getPackageClasses(String[] packageNames) {
        if (packageNames != null && packageNames.length > 0) {
            Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
            for (String names : packageNames) {
                Set<Class<?>> set = ClassUtils.getClasses(names);
                if (set.size() > 0) {
                    classes.addAll(set);
                }
            }
            return classes;
        }
        return null;
    }

    private void registerBeanDefinition(Class<?> type, ConfigurableApplicationContext context) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(type);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        definition.getPropertyValues().add("origin", type);
        definition.setBeanClass(RemoteClientFactoryBean.class);
        definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) context.getBeanFactory();
        beanFactory.registerBeanDefinition(type.getSimpleName(), definition);
    }
}
