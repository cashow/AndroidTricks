package com.cashow.daggerdemo.demo8;

import com.cashow.daggerdemo.common.model.TestObject4;

import dagger.Module;
import dagger.Provides;

@Module
public class Demo8Module {
    @Provides
    @ReleaseScope
    static TestObject4 provideTestObject4() {
        return new TestObject4();
    }
}
