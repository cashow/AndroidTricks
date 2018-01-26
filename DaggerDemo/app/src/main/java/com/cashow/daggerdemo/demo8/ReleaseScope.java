package com.cashow.daggerdemo.demo8;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

import dagger.releasablereferences.CanReleaseReferences;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@CanReleaseReferences
@Scope
public @interface ReleaseScope {
}
