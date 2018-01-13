package com.cashow.hencoderdemo.chapter_1_7;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AnimatorSet1View extends ImageView {
    private PointF pointF;
    private Paint paint;
    private int color;

    public AnimatorSet1View(Context context) {
        super(context);
        init();
    }

    public AnimatorSet1View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatorSet1View(Context context, AttributeSet attrs, int defStyleAttr) {
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
         * AnimatorSet 多个动画配合执行
         * 有的时候，你不止需要在一个动画中改变多个属性，还会需要多个动画配合工作，
         * 比如，在内容的大小从 0 放大到 100% 大小后开始移动。
         * 这种情况使用 PropertyValuesHolder 是不行的，因为这些属性如果放在同一个动画中，
         * 需要共享动画的开始时间、结束时间、Interpolator 等等一系列的设定，这样就不能有先后次序地执行动画了。
         * 这就需要用到 AnimatorSet 了。
         */
        PointF startPoint = new PointF(100, 100);
        PointF endPoint = new PointF(300, 300);

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofObject(this, "color", new HsvEvaluator(), 0xffff0000, 0xff00ff00);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofObject(this, "pointF", new PointFEvaluator(), startPoint, endPoint);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2500);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                postDelayed(() -> startAnim(), 1000);
            }
        });
        animatorSet.playSequentially(objectAnimator1, objectAnimator2);
        animatorSet.start();
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setPointF(PointF pointF) {
        this.pointF = pointF;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        canvas.drawCircle(pointF.x, pointF.y, 100, paint);
    }
}
