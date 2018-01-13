package com.cashow.hencoderdemo.chapter_1_6;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import com.cashow.hencoderdemo.R;

public class TranslationXView extends ImageView {

    public TranslationXView(Context context) {
        super(context);
        init();
    }

    public TranslationXView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TranslationXView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        viewPropertyAnimator.translationX(300);
        viewPropertyAnimator.withEndAction(() -> postDelayed(() -> startAnim(), 1000));
    }
}
