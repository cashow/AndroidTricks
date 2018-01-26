package com.cashow.daggerdemo.demo3;

import com.cashow.daggerdemo.common.model.TestObject3;

import dagger.Component;

@Component(modules = Demo3Module.class)
public interface Demo3Component {
    TestObject3 testObject3();
}
