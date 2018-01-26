package com.cashow.daggerdemo.demo6;

import com.cashow.daggerdemo.common.model.TestObject2;

import dagger.Component;

@Component(modules = Demo6Module.class)
public interface Demo6Component {
    TestObject2 testObject2();
    void inject(Demo6Activity activity);
}
