package com.cashow.daggerdemo.demo6;

import com.cashow.daggerdemo.common.model.TestObject2;

import dagger.Module;
import dagger.Provides;

@Module
public class Demo6Module {
    @Provides
    static TestObject2 provideTestObject2() {
        return new TestObject2(String.valueOf((int)(Math.random() * 100)), 456);
    }
}
