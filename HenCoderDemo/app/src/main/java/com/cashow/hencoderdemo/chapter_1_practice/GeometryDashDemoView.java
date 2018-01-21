package com.cashow.hencoderdemo.chapter_1_practice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

public class GeometryDashDemoView extends FrameLayout {
    private RectView rectView;
    private TriangleView triangleView;
    private GroundView groundView;
    private BgView bgView;

    private int startJumpProgress = 60;
    private int endJumpProgress = 90;
    private int middleJumpProgress;

    public GeometryDashDemoView(Context context) {
        super(context);
        init(context);
    }

    public GeometryDashDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GeometryDashDemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        middleJumpProgress = (endJumpProgress + startJumpProgress) / 2;

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
        setLayoutParams(params);

        bgView = new BgView(context);
        addView(bgView);

        rectView = new RectView(context);
        rectView.setX(200);
        rectView.setY(350);
        addView(rectView);

        triangleView = new TriangleView(context);
        triangleView.setY(350);
        addView(triangleView);

        groundView = new GroundView(context);
        groundView.setY(400);
        addView(groundView);

        post(this::startAnim);
    }

    private void startAnim() {
        float initY = rectView.getY();
        int startTriangleX = getWidth();
        int endTriangleX = -triangleView.getWidth();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            int val = (Integer) animation.getAnimatedValue();
            rectView.setY(initY + getRectYProgress(val) * -100);
            rectView.setRotation(getRectRotationProgress(val) * 360);

            triangleView.setX(getProgressValue(startTriangleX, endTriangleX, val / 100.0));
        });
        valueAnimator.setDuration(3000);
        valueAnimator.setTarget(rectView);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                postDelayed(() -> startAnim(), 1000);
            }
        });
        valueAnimator.start();
    }

    private float getRectYProgress(int progress) {
        if (progress < startJumpProgress || progress > endJumpProgress) {
            return 0;
        }
        float newProgress = Math.abs(progress - middleJumpProgress);
        newProgress = newProgress / (middleJumpProgress - startJumpProgress);
        newProgress = 1.0f - newProgress;
        return newProgress;
    }

    private float getRectRotationProgress(int progress) {
        if (progress < startJumpProgress || progress > endJumpProgress) {
            return 0;
        }
        float newProgress = progress - startJumpProgress;
        newProgress = newProgress / (endJumpProgress - startJumpProgress);
        return newProgress;
    }

    /**
     * 获取 [start, end] 范围内进度为progress的值
     * @param start 初始值
     * @param end 最终值
     * @param progress 当前的进度, 0 <= progress <= 1
     * @return 当前的值
     */
    private int getProgressValue(int start, int end, double progress) {
        return (int) (start + (end - start) * progress);
    }
}
