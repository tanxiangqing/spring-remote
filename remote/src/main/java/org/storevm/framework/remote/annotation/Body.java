package org.storevm.framework.remote.annotation;

import java.lang.annotation.*;

/**
 * @author Jack
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Body {
    boolean required() default true;
    boolean compress() default false;
}
