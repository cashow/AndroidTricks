package com.cashow.daggerdemo.demo10;

import com.cashow.daggerdemo.common.model.TestObject2;

import dagger.Component;

@Component(modules = Demo10Module.class)
public interface Demo10Component {
    TestObject2 testObject2();

    void inject(Demo10Activity activity);

    Demo10SubComponent demo10SubComponent();
}
