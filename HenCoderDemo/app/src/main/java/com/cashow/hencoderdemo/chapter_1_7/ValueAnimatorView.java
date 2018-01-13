package com.cashow.hencoderdemo.chapter_1_7;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

public class ValueAnimatorView extends View {
    private Paint paint;
    private int circleX;
    private int circleY;

    public ValueAnimatorView(Context context) {
        super(context);
        init();
    }

    public ValueAnimatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ValueAnimatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(400, 400);
        setLayoutParams(params);

        post(() -> startAnim());
    }

    private void startAnim() {
        ValueAnimator imageBodyAnimator = ValueAnimator.ofInt(0, 100);
        imageBodyAnimator.setInterpolator(new AccelerateInterpolator());
        imageBodyAnimator.addUpdateListener(animation -> {
            int val = (Integer) animation.getAnimatedValue();
            int circleX = 100 + val;
            int circleY = 100 + val;
            setCirclePosition(circleX, circleY);
        });
        imageBodyAnimator.setDuration(2000);
        imageBodyAnimator.setTarget(this);
        imageBodyAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                postDelayed(() -> startAnim(), 1000);
            }
        });
        imageBodyAnimator.start();
    }

    public void setCirclePosition(int circleX, int circleY) {
        this.circleX = circleX;
        this.circleY = circleY;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(circleX, circleY, 100, paint);
    }
}
