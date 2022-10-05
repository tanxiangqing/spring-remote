package org.storevm.framework.remote.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Jack
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Path {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}
