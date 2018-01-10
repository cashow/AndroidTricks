package com.cashow.hencoderdemo.chapter_1_4;

import com.cashow.hencoderdemo.common.BaseActivity;

public class Chapter_1_4_Activity extends BaseActivity {
    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{ClipView.class, CanvasTransformationView.class, MatrixTransformationView.class,
                CameraTransformationView.class};
    }
}
