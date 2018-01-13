package com.cashow.hencoderdemo.chapter_1_6;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import com.cashow.hencoderdemo.R;

public class Duration600View extends ImageView {

    public Duration600View(Context context) {
        super(context);
        init();
    }

    public Duration600View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Duration600View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(200, 200);
        setLayoutParams(params);
        setImageResource(R.drawable.ic_launcher);

        post(() -> startAnim());
    }

    private void startAnim() {
        setTranslationX(0);
        ViewPropertyAnimator viewPropertyAnimator = animate();
        viewPropertyAnimator.setDuration(600);
        viewPropertyAnimator.translationX(300);
        postDelayed(() -> startAnim(), 1000);
    }
}
