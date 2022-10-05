package org.storevm.framework.remote.annotation;

import java.lang.annotation.*;

/**
 * To mark an interface is a remote invoker.<br>
 *
 * @author Jack
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Remote {
    String host() default "localhost";

    String scheme() default "http";
}
