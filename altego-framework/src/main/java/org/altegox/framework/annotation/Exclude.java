package org.altegox.framework.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Exclude {

    Class<? extends Annotation>[] annotation() default {};

}
