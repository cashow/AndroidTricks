package com.cashow.daggerdemo.demo4;

import com.cashow.daggerdemo.common.model.TestObject;
import com.cashow.daggerdemo.common.model.TestObject2;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class Demo4SingletonModule {
    @Singleton
    @Provides
    static TestObject2 provideTestObject2() {
        return new TestObject2(String.valueOf((int)(Math.random() * 100)), 456);
    }

    @Provides
    static TestObject provideTestObject() {
        TestObject testObject = new TestObject();
        testObject.setName(String.valueOf((int)(Math.random() * 100)));
        return testObject;
    }
}
