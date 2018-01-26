package com.cashow.daggerdemo.demo9;

import com.cashow.daggerdemo.common.model.TestObject2;
import com.cashow.daggerdemo.demo8.ReleaseScope;

import dagger.Component;

@ReleaseScope
@Component(modules = Demo9Module.class)
public interface Demo9Component {
    TestObject2 testObject2();

    void inject(Demo9Activity activity);
}
