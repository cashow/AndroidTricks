package com.cashow.hencoderdemo.chapter_1_7;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cashow.hencoderdemo.common.BaseActivity;

public class Chapter_1_7_Activity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutContainer.addView(new ArgbEvaluator1View(context));
        addDivider();
        layoutContainer.addView(new ArgbEvaluator2View(context));
        addDivider();
        layoutContainer.addView(new HsvEvaluatorView(context));
        addDivider();
        layoutContainer.addView(new PointFEvaluatorView(context));
        addDivider();
        layoutContainer.addView(new PropertyValuesHolderView(context));
        addDivider();
        layoutContainer.addView(new AnimatorSet1View(context));
        addDivider();
        layoutContainer.addView(new AnimatorSet2View(context));
        addDivider();
        layoutContainer.addView(new KeyFrame1View(context));
        addDivider();
        layoutContainer.addView(new KeyFrame2View(context));
        addDivider();
        layoutContainer.addView(new ValueAnimatorView(context));
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
