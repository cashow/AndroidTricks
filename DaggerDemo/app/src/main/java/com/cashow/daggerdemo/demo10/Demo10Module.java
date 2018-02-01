package com.cashow.daggerdemo.demo10;

import com.cashow.daggerdemo.common.model.TestObject2;
import com.cashow.daggerdemo.common.model.TestObject5;

import dagger.Module;
import dagger.Provides;

@Module
public class Demo10Module {
    @Provides
    static TestObject2 provideTestObject2() {
        return new TestObject2("name_2", 456);
    }

    @Provides
    static TestObject5 provideTestObject5() {
        return new TestObject5("name_5", 456);
    }
}
