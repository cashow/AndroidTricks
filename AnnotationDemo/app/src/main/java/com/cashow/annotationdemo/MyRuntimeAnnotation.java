package com.cashow.annotationdemo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 运行时注解
 * Retention 的值为 RUNTIME 时, 注解会保留到运行时, 因此可以使用反射来解析注解。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MyRuntimeAnnotation {
    String name() default "lalala";

    int age() default 18;
}
