package com.cashow.daggerdemo.demo5;

import android.os.Bundle;

import com.cashow.daggerdemo.common.BaseActivity;
import com.cashow.daggerdemo.common.model.TestObject2;

public class Demo5Activity extends BaseActivity {
    private TestObject2 testObject2Normal1;
    private TestObject2 testObject2Normal2;
    private TestObject2 testObject2Singleton1;
    private TestObject2 testObject2Singleton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        textView.setText(testObject2Normal1.toString() + "\n" + testObject2Normal2.toString() + "\n\n" + testObject2Singleton1.toString() + "\n" + testObject2Singleton2.toString());
    }

    /**
     * @Reusable 作用域不像其他作用域那样要和 component 绑定。
     * 对于 @Reusable 标记的类，每个 component 在调用绑定时会缓存生成的实例。
     */
    private void init() {
        Demo5Component daggerDemo5Component = DaggerDemo5Component.create();
        testObject2Normal1 = daggerDemo5Component.testObject2();
        testObject2Normal2 = daggerDemo5Component.testObject2();

        Demo5ReusableComponent demo5ReusableComponent = DaggerDemo5ReusableComponent.create();
        testObject2Singleton1 = demo5ReusableComponent.testObject2();
        testObject2Singleton2 = demo5ReusableComponent.testObject2();
    }
}
