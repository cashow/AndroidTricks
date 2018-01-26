package com.cashow.daggerdemo.demo9;

import android.os.Bundle;
import android.view.View;

import com.cashow.daggerdemo.R;
import com.cashow.daggerdemo.common.BaseActivity;
import com.cashow.daggerdemo.common.model.TestObject;
import com.cashow.daggerdemo.common.model.TestObject2;

import javax.inject.Inject;

import butterknife.OnClick;
import dagger.Lazy;

public class Demo9Activity extends BaseActivity {
    @Inject
    TestObject testObject;
    @Inject
    Lazy<TestObject2> testObject2Lazy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        buttonRelease.setVisibility(View.VISIBLE);
        buttonRelease.setText("lazy TestObject2");
    }

    /**
     * 从 log 中可以看到，在 init() 方法会立即调用 TestObject 的构造函数，
     * 在点击按钮后才会调用 TestObject2 的构造函数
     */
    private void init() {
        Demo9Component demo9Component = DaggerDemo9Component.create();
        demo9Component.inject(this);
    }

    @OnClick(R.id.button_release)
    void onButtonClick() {
        testObject2Lazy.get();
    }
}
