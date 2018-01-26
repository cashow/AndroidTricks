package com.cashow.daggerdemo.demo8;

import com.cashow.daggerdemo.common.model.TestObject4;

import dagger.Component;

@ReleaseScope
@Component(modules = Demo8Module.class)
public interface Demo8Component {
    TestObject4 testObject4();
    void inject(Demo8Activity activity);
}
