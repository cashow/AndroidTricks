package com.cashow.daggerdemo.demo3;

import android.os.Bundle;

import com.cashow.daggerdemo.common.BaseActivity;
import com.cashow.daggerdemo.common.model.TestObject3;

public class Demo3Activity extends BaseActivity {
    private TestObject3 testObject3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        textView.setText(testObject3.testObject.toString() + "\n" + testObject3.testObject2.toString());
    }

    /**
     * 在这里通过 dagger 自动初始化 testObject3，
     * 其中 testObject3 的构造函数需要 testObject 和 testObject2 的实例，
     * dagger 会自动创建 testObject 和 testObject2，之后再调用 testObject3 的构造函数
     */
    private void init() {
        Demo3Component demo3Component = DaggerDemo3Component.create();
        testObject3 = demo3Component.testObject3();
    }
}
