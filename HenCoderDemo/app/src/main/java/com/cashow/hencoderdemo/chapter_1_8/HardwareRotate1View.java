package com.cashow.hencoderdemo.chapter_1_8;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cashow.hencoderdemo.R;

public class HardwareRotate1View extends ImageView {

    public HardwareRotate1View(Context context) {
        super(context);
        init();
    }

    public HardwareRotate1View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HardwareRotate1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setImageResource(R.drawable.ic_launcher);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(300, 300);
        setLayoutParams(params);

        post(() -> startAnim());
    }

    private void startAnim() {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "rotationY", 0, 180);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setLayerType(LAYER_TYPE_NONE, null);
                postDelayed(() -> startAnim(), 1000);
            }
        });
        objectAnimator.start();
    }
}
