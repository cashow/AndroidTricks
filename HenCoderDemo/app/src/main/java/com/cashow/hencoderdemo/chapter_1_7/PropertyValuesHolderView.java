package com.cashow.hencoderdemo.chapter_1_7;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class PropertyValuesHolderView extends View {
    private PointF pointF;
    private Paint paint;
    private int color;

    public PropertyValuesHolderView(Context context) {
        super(context);
        init();
    }

    public PropertyValuesHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PropertyValuesHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
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
         * 很多时候，你在同一个动画中会需要改变多个属性，例如在改变透明度的同时改变尺寸。如果使用 ViewPropertyAnimator，你可以直接用连写的方式来在一个动画中同时改变多个属性：
         * view.animate()
         *    .scaleX(1)
         *    .scaleY(1)
         *    .alpha(1);
         *
         * 而对于 ObjectAnimator，是不能这么用的。不过你可以使用 PropertyValuesHolder 来同时在一个动画中改变多个属性。
         * 如果有多个属性需要修改，可以把它们放在不同的 PropertyValuesHolder 中，然后使用  ofPropertyValuesHolder() 统一放进 Animator。这样你就不用为每个属性单独创建一个 Animator 分别执行了。
         */
        PointF startPoint = new PointF(100, 100);
        PointF endPoint = new PointF(300, 300);

        PropertyValuesHolder holder1 = PropertyValuesHolder.ofObject("color", new HsvEvaluator(), 0xffff0000, 0xff00ff00);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofObject("pointF", new PointFEvaluator(), startPoint, endPoint);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, holder1, holder2);
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
