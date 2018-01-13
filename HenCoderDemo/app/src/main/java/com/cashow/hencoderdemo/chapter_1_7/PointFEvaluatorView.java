package com.cashow.hencoderdemo.chapter_1_7;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class PointFEvaluatorView extends View {
    private PointF pointF;
    private Paint paint;

    public PointFEvaluatorView(Context context) {
        super(context);
        init();
    }

    public PointFEvaluatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointFEvaluatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        pointF = new PointF(100, 100);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(400, 400);
        setLayoutParams(params);

        post(() -> startAnim());
    }

    private void startAnim() {
        /**
         * 借助于 TypeEvaluator，属性动画就可以通过 ofObject() 来对不限定类型的属性做动画了。方式很简单：
         * 1. 为目标属性写一个自定义的 TypeEvaluator
         * 2. 使用 ofObject() 来创建 Animator，并把自定义的 TypeEvaluator 作为参数填入
         */
        PointF startPoint = new PointF(100, 100);
        PointF endPoint = new PointF(300, 300);

        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(this, "pointF", new PointFEvaluator(), startPoint, endPoint);
        objectAnimator.setDuration(2500);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                postDelayed(() -> startAnim(), 1000);
            }
        });
        objectAnimator.start();
    }

    public void setPointF(PointF pointF) {
        this.pointF = pointF;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(pointF.x, pointF.y, 100, paint);
    }
}
