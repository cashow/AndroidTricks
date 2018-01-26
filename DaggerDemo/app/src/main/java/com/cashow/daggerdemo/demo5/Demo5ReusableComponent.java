package com.cashow.daggerdemo.demo5;

import com.cashow.daggerdemo.common.model.TestObject2;

import dagger.Component;

@Component(modules = Demo5ReusableModule.class)
public interface Demo5ReusableComponent {
    TestObject2 testObject2();
}
