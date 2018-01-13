package com.cashow.hencoderdemo.chapter_1_7;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
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

import com.cashow.hencoderdemo.R;

public class KeyFrame1View extends ImageView {

    public KeyFrame1View(Context context) {
        super(context);
        init();
    }

    public KeyFrame1View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyFrame1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setImageResource(R.drawable.ic_launcher);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(400, 400);
        setLayoutParams(params);

        post(() -> startAnim());
    }

    private void startAnim() {
        /**
         * PropertyValuesHolders.ofKeyframe() 把同一个属性拆分
         * 除了合并多个属性和调配多个动画，你还可以在 PropertyValuesHolder 的基础上更进一步，
         * 通过设置  Keyframe （关键帧），把同一个动画属性拆分成多个阶段。例如，你可以让一个进度增加到 100% 后再「反弹」回来。
         *
         * Keyframe ofFloat(float fraction, float value)
         * fraction：表示当前的显示进度，即从加速器中 getInterpolation()函数的返回值；
         * value：表示当前应该在的位置 比如 Keyframe.ofFloat(0, 0)表示动画进度为 0 时，动画所在的数值位置为 0；
         * Keyframe.ofFloat(0.25f, -20f)表示动画进度为 25%时，动画所在的数值位置为-20；
         * Keyframe.ofFloat(1f,0)表示动画结束时，动画所在的数值位置为 0；
         */
        Keyframe keyframe1 = Keyframe.ofFloat(0, 0);
        Keyframe keyframe2 = Keyframe.ofFloat(0.5f, 100);
        Keyframe keyframe3 = Keyframe.ofFloat(1f, 60);

        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("rotation", keyframe1, keyframe2, keyframe3);

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
}
