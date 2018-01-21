package com.cashow.hencoderdemo.chapter_1_practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class RectView extends View {
    private Paint rectPaint;

    public RectView(Context context) {
        super(context);
        init();
    }

    public RectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(10);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50, 50);
        setLayoutParams(params);

//        post(() -> startAnim());
    }

    private void startAnim() {
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "rotationY", 0, 180);
//        objectAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                postDelayed(() -> startAnim(), 1000);
//            }
//        });
//        objectAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, 50, 50, rectPaint);
    }
}
