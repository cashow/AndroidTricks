package com.cashow.daggerdemo.demo2;

import com.cashow.daggerdemo.common.model.TestObject;
import com.cashow.daggerdemo.common.model.TestObject2;

import dagger.Component;

@Component(modules = Demo2Module.class)
public interface Demo2Component {
    TestObject testObject();
    TestObject2 testObject2();
}
