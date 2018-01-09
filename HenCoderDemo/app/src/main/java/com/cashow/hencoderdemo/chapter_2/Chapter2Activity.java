package com.cashow.hencoderdemo.chapter_2;

import com.cashow.hencoderdemo.common.BaseActivity;

public class Chapter2Activity extends BaseActivity {
    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{PaintColorView.class, PorterDuffModeView.class,
                ColorFilterView.class, XferModeView.class, PaintView.class, PaintFilterView.class,
                PaintPathEffectView.class, MaskFilterView.class, GetPathView.class, PaintInitView.class};
    }
}
