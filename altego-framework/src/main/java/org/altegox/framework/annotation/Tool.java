package org.altegox.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Tool {
    String[] defaultParams() default {};

    Param[] params() default {};

    String description() default "";

    boolean remoteTool() default false;
}

