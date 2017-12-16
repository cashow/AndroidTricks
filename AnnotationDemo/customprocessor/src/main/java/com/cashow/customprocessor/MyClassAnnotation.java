package com.cashow.customprocessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 编译时注解
 * 解析编译时注解需要继承 AbstractProcessor 类, 实现其抽象方法
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface MyClassAnnotation {
    String name() default "undefined";

    String text() default "";
}
