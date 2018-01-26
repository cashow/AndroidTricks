package com.cashow.daggerdemo.demo9;

import com.cashow.daggerdemo.common.model.TestObject2;
import com.cashow.daggerdemo.demo8.ReleaseScope;

import dagger.Module;
import dagger.Provides;

@Module
public class Demo9Module {
    @Provides
    @ReleaseScope
    static TestObject2 provideTestObject2() {
        return new TestObject2("name_2", 456);
    }
}
