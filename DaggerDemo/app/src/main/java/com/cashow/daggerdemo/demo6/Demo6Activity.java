package com.cashow.daggerdemo.demo6;

import android.os.Bundle;

import com.cashow.daggerdemo.common.BaseActivity;
import com.cashow.daggerdemo.common.model.TestObject2;

import javax.inject.Inject;

public class Demo6Activity extends BaseActivity {
    @Inject
    TestObject2 testObject2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        textView.setText(testObject2.toString());
    }

    /**
     */
    private void init() {
        Demo6Component demo6Component = DaggerDemo6Component.create();
        demo6Component.inject(this);
    }
}
