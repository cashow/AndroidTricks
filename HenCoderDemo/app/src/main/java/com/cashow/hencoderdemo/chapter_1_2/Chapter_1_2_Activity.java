package com.cashow.hencoderdemo.chapter_1_2;

import com.cashow.hencoderdemo.common.BaseActivity;

public class Chapter_1_2_Activity extends BaseActivity {
    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{PaintColorView.class, PorterDuffModeView.class,
                ColorFilterView.class, XferModeView.class, PaintView.class, PaintFilterView.class,
                PaintPathEffectView.class, MaskFilterView.class, GetPathView.class, PaintInitView.class};
    }
}
