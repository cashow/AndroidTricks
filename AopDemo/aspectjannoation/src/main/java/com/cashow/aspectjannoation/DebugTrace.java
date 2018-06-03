package com.cashow.aspectjannoation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Target({ METHOD, CONSTRUCTOR}) @Retention(CLASS)
public @interface DebugTrace {
}
