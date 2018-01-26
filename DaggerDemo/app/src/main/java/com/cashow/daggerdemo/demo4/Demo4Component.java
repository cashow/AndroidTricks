package com.cashow.daggerdemo.demo4;

import com.cashow.daggerdemo.common.model.TestObject2;

import dagger.Component;

@Component(modules = Demo4Module.class)
public interface Demo4Component {
    TestObject2 testObject2();
}
