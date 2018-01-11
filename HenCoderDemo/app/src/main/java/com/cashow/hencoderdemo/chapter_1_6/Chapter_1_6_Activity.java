package com.cashow.hencoderdemo.chapter_1_6;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cashow.hencoderdemo.common.BaseActivity;

public class Chapter_1_6_Activity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{};
    }

    @Override
    protected boolean isAutoAddDemoView() {
        return false;
    }
}
