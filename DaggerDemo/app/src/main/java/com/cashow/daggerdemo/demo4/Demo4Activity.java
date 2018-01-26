package com.cashow.daggerdemo.demo4;

import android.os.Bundle;

import com.cashow.daggerdemo.common.BaseActivity;
import com.cashow.daggerdemo.common.model.TestObject;
import com.cashow.daggerdemo.common.model.TestObject2;

public class Demo4Activity extends BaseActivity {
    private TestObject2 testObject2Normal1;
    private TestObject2 testObject2Normal2;
    private TestObject2 testObject2Singleton1;
    private TestObject2 testObject2Singleton2;
    private TestObject testObjectSingleton1;
    private TestObject testObjectSingleton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        String text = testObject2Normal1.toString() + "\n" +
                testObject2Normal2.toString() + "\n\n" +
                testObject2Singleton1.toString() + "\n" +
                testObject2Singleton2.toString() + "\n\n" +
                testObjectSingleton1.toString() + "\n" +
                testObjectSingleton2.toString();
        textView.setText(text);
    }

    /**
     * @Provides 方法或者可注入的类加上 @Singleton 注解，Dagger 会给所有依赖的变量返回同一个实例。
     * 因为 Demo4Module 里的 providetestObject2() 方法没有 @Singleton 注解，两次取到的 testObject2Normal1 和 testObject2Normal2 是不一样的
     * Demo4SingletonModule 里，provideTestObject2() 方法有 @Singleton 注解，provideTestObject() 方法没有 @Singleton 注解，Demo4SingletonComponent 有 @Singleton 注解，
     * 这时两次取到的 TestObject2 是一样的，TestObject 是不一样的。
     */
    private void init() {
        Demo4Component daggerDemo4Component = DaggerDemo4Component.create();
        testObject2Normal1 = daggerDemo4Component.testObject2();
        testObject2Normal2 = daggerDemo4Component.testObject2();

        Demo4SingletonComponent daggerDemo4SingletonComponent = DaggerDemo4SingletonComponent.create();
        testObject2Singleton1 = daggerDemo4SingletonComponent.testObject2();
        testObjectSingleton1 = daggerDemo4SingletonComponent.testObject();

        testObject2Singleton2 = daggerDemo4SingletonComponent.testObject2();
        testObjectSingleton2 = daggerDemo4SingletonComponent.testObject();
    }
}
