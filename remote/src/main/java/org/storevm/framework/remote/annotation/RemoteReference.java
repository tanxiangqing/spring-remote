package org.storevm.framework.remote.annotation;

import java.lang.annotation.*;

/**
 * @author jack
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RemoteReference {
}
