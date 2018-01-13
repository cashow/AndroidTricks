package com.cashow.hencoderdemo.chapter_1_7;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

public class KeyFrame2View extends ImageView {
    private PointF pointF;
    private Paint paint;

    public KeyFrame2View(Context context) {
        super(context);
        init();
    }

    public KeyFrame2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyFrame2View(Context context, AttributeSet attrs, int defStyleAttr) {
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
        PointF startPoint = new PointF(100, 100);
        PointF middlePoint = new PointF(300, 300);
        PointF endPoint = new PointF(200, 200);

        Keyframe keyframe1 = Keyframe.ofObject(0.0f, startPoint);
        Keyframe keyframe2 = Keyframe.ofObject(0.5f, middlePoint);
        Keyframe keyframe3 = Keyframe.ofObject(1.0f, endPoint);

        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("pointF", keyframe1, keyframe2, keyframe3);
        holder.setEvaluator(new PointFEvaluator());

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, holder);
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
