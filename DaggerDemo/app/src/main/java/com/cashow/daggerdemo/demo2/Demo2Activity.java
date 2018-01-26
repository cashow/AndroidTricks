package com.cashow.daggerdemo.demo2;

import android.os.Bundle;

import com.cashow.daggerdemo.common.BaseActivity;
import com.cashow.daggerdemo.common.model.TestObject;
import com.cashow.daggerdemo.common.model.TestObject2;

public class Demo2Activity extends BaseActivity {
    private TestObject testObject;
    private TestObject2 testObject2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        textView.setText(testObject.toString() + "\n" + testObject2.toString());
    }

    /**
     * 在这里通过 dagger 自动初始化 testObject 和 testObject2
     * TestObject 类里有 @Inject 注解的无参数的构造函数，dagger 会自动调用这个构造函数生成 testObject 实例
     * 在 Demo2Module 里有 @Provides 注解的 providetestObject2() 方法，dagger 会自动调用这个方法生成 testObject2 实例
     */
    private void init() {
        Demo2Component demo2Component = DaggerDemo2Component.create();
        testObject = demo2Component.testObject();
        testObject2 = demo2Component.testObject2();
    }
}
