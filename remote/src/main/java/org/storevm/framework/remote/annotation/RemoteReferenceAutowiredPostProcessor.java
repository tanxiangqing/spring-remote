package org.storevm.framework.remote.annotation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.storevm.framework.remote.core.RemoteClientFactoryBean;

import java.lang.reflect.Field;

@Slf4j
@Component
public class RemoteReferenceAutowiredPostProcessor implements SmartInstantiationAwareBeanPostProcessor, ApplicationContextAware {
    private ApplicationContext context;

    /**
     * constructor
     */
    public RemoteReferenceAutowiredPostProcessor() {
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = FieldUtils.getFieldsWithAnnotation(bean.getClass(), RemoteReference.class);
        if (fields != null && fields.length > 0) {
            for (int i = 0, n = fields.length; i < n; i++) {
                Object proxy = getProxy(fields[i].getType());
                fields[i].setAccessible(true);
                try {
                    fields[i].set(bean, proxy);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    log.error("proxy bean autowired to occur exception.", ex);
                }
            }
        }
        return bean;
    }

    private Object getProxy(Class<?> type) {
        try {
            return this.context.getBean(type);
        } catch (BeansException ex) {
            log.warn("no bean in application context, type={}", type);
            registerBeanDefinition(type);
            return this.context.getBean(type);
        }
    }

    private void registerBeanDefinition(Class<?> type) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(type);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        definition.getPropertyValues().add("origin", type);
        definition.setBeanClass(RemoteClientFactoryBean.class);
        definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) this.context;
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) context.getBeanFactory();
        beanFactory.registerBeanDefinition(type.getSimpleName(), definition);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
