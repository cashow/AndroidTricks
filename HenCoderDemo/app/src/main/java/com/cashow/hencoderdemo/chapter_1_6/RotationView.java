package com.cashow.hencoderdemo.chapter_1_6;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import com.cashow.hencoderdemo.R;

public class RotationView extends ImageView {

    public RotationView(Context context) {
        super(context);
        init();
    }

    public RotationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotationView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        setRotation(0);
        ViewPropertyAnimator viewPropertyAnimator = animate();
        viewPropertyAnimator.rotation(360);
        viewPropertyAnimator.withEndAction(() -> postDelayed(() -> startAnim(), 1000));
    }
}
