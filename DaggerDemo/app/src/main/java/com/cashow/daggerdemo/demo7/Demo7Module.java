package com.cashow.daggerdemo.demo7;

import com.cashow.daggerdemo.common.model.TestObject4;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class Demo7Module {
    @Provides
    @Singleton
    static TestObject4 provideTestObject4() {
        return new TestObject4();
    }
}
