package com.cashow.hencoderdemo.chapter_1_7;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ArgbEvaluator2View extends View {
    private int color;
    private Paint paint;

    public ArgbEvaluator2View(Context context) {
        super(context);
        init();
    }

    public ArgbEvaluator2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArgbEvaluator2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(200, 200);
        setLayoutParams(params);

        post(() -> startAnim());
    }

    private void startAnim() {
        setColor(0xffff0000);
        ObjectAnimator objectAnimator = ObjectAnimator.ofArgb(this, "color", 0xffff0000, 0xff00ff00);
        objectAnimator.setDuration(2500);
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
        canvas.drawCircle(100, 100, 100, paint);
    }
}
