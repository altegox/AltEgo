package org.altegox.framework.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String param();

    String description() default "";

    boolean required() default true;
}