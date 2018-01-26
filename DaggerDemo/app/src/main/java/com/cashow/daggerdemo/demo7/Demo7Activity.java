package com.cashow.daggerdemo.demo7;

import android.os.Bundle;

import com.cashow.daggerdemo.common.BaseActivity;
import com.cashow.daggerdemo.common.model.TestObject4;

public class Demo7Activity extends BaseActivity {
    private Demo7Component demo7Component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    /**
     * 注入了 TestObject4 对象，打开这个 Activity 后内存占用从 40MB 飙升到了 240MB，并且强制 gc 也没有用
     */
    private void init() {
        demo7Component = DaggerDemo7Component.create();
        TestObject4 testObject4 = demo7Component.testObject4();
    }
}
