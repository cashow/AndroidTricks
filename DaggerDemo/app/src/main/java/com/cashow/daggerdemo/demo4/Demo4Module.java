package com.cashow.daggerdemo.demo4;

import com.cashow.daggerdemo.common.model.TestObject2;

import dagger.Module;
import dagger.Provides;

@Module
public class Demo4Module {
    @Provides
    static TestObject2 providetestObject2() {
        return new TestObject2(String.valueOf((int)(Math.random() * 100)), 456);
    }
}
