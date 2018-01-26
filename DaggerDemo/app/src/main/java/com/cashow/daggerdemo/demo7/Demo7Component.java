package com.cashow.daggerdemo.demo7;

import com.cashow.daggerdemo.common.model.TestObject4;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = Demo7Module.class)
public interface Demo7Component {
    TestObject4 testObject4();
    void inject(Demo7Activity activity);
}
