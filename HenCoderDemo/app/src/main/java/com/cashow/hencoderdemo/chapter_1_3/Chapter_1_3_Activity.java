package com.cashow.hencoderdemo.chapter_1_3;

import com.cashow.hencoderdemo.common.BaseActivity;

public class Chapter_1_3_Activity extends BaseActivity {
    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{NewDrawTextView.class, TextEffectView.class, TextMeasureView.class};
    }
}
