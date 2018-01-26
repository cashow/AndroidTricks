package com.cashow.daggerdemo.demo2;

import com.cashow.daggerdemo.common.model.TestObject2;

import dagger.Module;
import dagger.Provides;

@Module
public class Demo2Module {
    @Provides static TestObject2 providetestObject2() {
        return new TestObject2("name_2", 456);
    }
}
