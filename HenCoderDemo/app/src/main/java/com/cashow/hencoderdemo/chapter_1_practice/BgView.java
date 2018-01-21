package com.cashow.hencoderdemo.chapter_1_practice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.cashow.hencoderdemo.chapter_1_7.HsvEvaluator;

public class BgView extends View {
    private int color;
    private Paint paint;

    public BgView(Context context) {
        super(context);
        init();
    }

    public BgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(params);

        post(() -> startAnim());
    }

    private void startAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "color", 0xFFffabc8, 0xFFff6d9f, 0xFFffabc8);
        objectAnimator.setEvaluator(new HsvEvaluator());
        objectAnimator.setDuration(3000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                postDelayed(() -> startAnim(), 1000);
            }
        });
        objectAnimator.start();
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }
}
