package com.cashow.hencoderdemo.chapter_1_8;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cashow.hencoderdemo.R;

public class HardwareRotate2View extends ImageView {

    public HardwareRotate2View(Context context) {
        super(context);
        init();
    }

    public HardwareRotate2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HardwareRotate2View(Context context, AttributeSet attrs, int defStyleAttr) {
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
        /**
         * withLayer() 可以自动完成 HardwareRotate1View 里的复杂操作
         */
        animate().rotationYBy(180).withLayer().withEndAction(() -> postDelayed(() -> startAnim(), 1000));
    }
}
