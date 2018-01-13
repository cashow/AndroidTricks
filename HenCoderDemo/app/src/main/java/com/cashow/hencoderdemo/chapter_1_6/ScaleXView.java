package com.cashow.hencoderdemo.chapter_1_6;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import com.cashow.hencoderdemo.R;

public class ScaleXView extends ImageView {

    public ScaleXView(Context context) {
        super(context);
        init();
    }

    public ScaleXView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaleXView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        setScaleX(1.0f);
        ViewPropertyAnimator viewPropertyAnimator = animate();
        viewPropertyAnimator.scaleX(2.0f);
        viewPropertyAnimator.withEndAction(() -> postDelayed(() -> startAnim(), 1000));
    }
}
