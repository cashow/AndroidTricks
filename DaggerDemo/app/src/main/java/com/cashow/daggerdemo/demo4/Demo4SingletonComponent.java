package com.cashow.daggerdemo.demo4;

import com.cashow.daggerdemo.common.model.TestObject;
import com.cashow.daggerdemo.common.model.TestObject2;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = Demo4SingletonModule.class)
public interface Demo4SingletonComponent {
    TestObject2 testObject2();
    TestObject testObject();
}
