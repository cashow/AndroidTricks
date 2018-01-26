package com.cashow.daggerdemo.demo5;

import com.cashow.daggerdemo.common.model.TestObject2;

import dagger.Module;
import dagger.Provides;

@Module
public class Demo5Module {
    @Provides
    static TestObject2 providetestObject2() {
        return new TestObject2(String.valueOf((int)(Math.random() * 100)), 456);
    }
}
