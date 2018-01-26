package com.cashow.daggerdemo.demo1;

import android.os.Bundle;

import com.cashow.daggerdemo.common.BaseActivity;
import com.cashow.daggerdemo.common.model.TestObject;
import com.cashow.daggerdemo.common.model.TestObject2;

public class Demo1Activity extends BaseActivity {
    private TestObject testObject;
    private TestObject2 testObject2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        textView.setText(testObject.toString() + "\n" + testObject2.toString());
    }

    /**
     * 在这里是手动初始化 testObject 和 testObject2
     */
    private void init() {
        testObject = new TestObject();
        testObject2 = new TestObject2("name_2", 456);
    }
}
