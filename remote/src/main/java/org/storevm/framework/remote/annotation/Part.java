package org.storevm.framework.remote.annotation;


import org.springframework.core.annotation.AliasFor;
import org.storevm.framework.remote.enums.PartType;

import java.lang.annotation.*;

/**
 * @author Jack
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Part {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    PartType type() default PartType.File;
}
