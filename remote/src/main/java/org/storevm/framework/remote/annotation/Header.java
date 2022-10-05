package org.storevm.framework.remote.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author jack
 */
@Repeatable(Headers.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface Header {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}
