package com.cashow.daggerdemo.demo5;

import com.cashow.daggerdemo.common.model.TestObject2;

import dagger.Component;

@Component(modules = Demo5Module.class)
public interface Demo5Component {
    TestObject2 testObject2();
}
