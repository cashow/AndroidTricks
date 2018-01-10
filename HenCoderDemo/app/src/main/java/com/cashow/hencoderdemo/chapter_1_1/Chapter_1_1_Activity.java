package com.cashow.hencoderdemo.chapter_1_1;

import com.cashow.hencoderdemo.common.BaseActivity;

public class Chapter_1_1_Activity extends BaseActivity {
    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{DrawColorView.class,
                DrawCircleView.class, DrawRectView.class, DrawPointView.class, DrawOvalView.class,
                DrawLineView.class, DrawRoundRectView.class, DrawArcView.class, DrawPathView.class,
                DrawBitmapView.class, DrawTextView.class};
    }
}
