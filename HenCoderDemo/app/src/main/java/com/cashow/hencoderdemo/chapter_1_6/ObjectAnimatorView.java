package com.cashow.hencoderdemo.chapter_1_6;

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

public class ObjectAnimatorView extends View {
    private Paint paint;
    float progress = 0;

    public ObjectAnimatorView(Context context) {
        super(context);
        init();
    }

    public ObjectAnimatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ObjectAnimatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#e33b63"));
        paint.setStyle(Paint.Style.FILL);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(300, 300);
        setLayoutParams(params);

        post(() -> startAnim());
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    private void startAnim() {
        setProgress(0);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", 0, 100);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                postDelayed(() -> startAnim(), 1000);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(0, 0, 200, 200, -110, progress, true, paint);
    }
}
