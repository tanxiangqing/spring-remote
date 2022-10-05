package org.storevm.framework.remote.annotation;

import org.springframework.core.annotation.AliasFor;
import org.storevm.framework.remote.enums.CallMethod;

import java.lang.annotation.*;

/**
 * To mark a method is a http endpoint.<br>
 *
 * @author Jack
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Call {
    @AliasFor("path")
    String value() default "";

    @AliasFor("value")
    String path() default "";

    CallMethod method();
}
