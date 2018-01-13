package com.cashow.hencoderdemo.chapter_1_6;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Path;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;

import com.cashow.hencoderdemo.R;

@SuppressLint("AppCompatCustomView")
public class InterpolatorView extends ImageView {
    private int viewType;

    public InterpolatorView(Context context, int viewType) {
        super(context);
        this.viewType = viewType;
        init();
    }

    public InterpolatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InterpolatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(200, 200);
        setLayoutParams(params);
        setImageResource(R.drawable.ic_launcher);

        post(() -> startAnim());
    }

    private void startAnim() {
        setTranslationX(0);
        /**
         * AccelerateDecelerateInterpolator: 先加速再减速。
         * 这是默认的 Interpolator。这个是一种最符合现实中物体运动的 Interpolator，
         * 它的动画效果看起来就像是物体从速度为 0 开始逐渐加速，
         * 然后再逐渐减速直到 0 的运动。它的速度 / 时间曲线以及动画完成度 / 时间曲线都是一条正弦 / 余弦曲线
         *
         * LinearInterpolator: 匀速
         *
         * AccelerateInterpolator: 持续加速
         * 在整个动画过程中，一直在加速，直到动画结束的一瞬间，直接停止。
         * 主要用在离场效果中
         *
         * DecelerateInterpolator: 持续减速直到 0
         * 动画开始的时候是最高速度，然后在动画过程中逐渐减速，直到动画结束的时候恰好减速到 0。
         * 主要用于入场效果
         *
         * AnticipateInterpolator: 先回拉一下再进行正常动画轨迹。效果看起来有点像投掷物体或跳跃等动作前的蓄力。
         * 如果是的平移动画，那么就是位置上的回拉；如果是放大动画，那么就是先缩小一下再放大
         *
         * OvershootInterpolator: 动画会超过目标值一些，然后再弹回来。效果看起来有点像你一屁股坐在沙发上后又被弹起来一点的感觉。
         *
         * AnticipateOvershootInterpolator: 上面这两个的结合版：开始前回拉，最后超过一些然后回弹。
         *
         * BounceInterpolator: 在目标值处弹跳。有点像玻璃球掉在地板上的效果。
         *
         * CycleInterpolator: 这个也是一个正弦 / 余弦曲线，不过它和 AccelerateDecelerateInterpolator 的区别是，
         * 它可以自定义曲线的周期，所以动画可以不到终点就结束，也可以到达终点后回弹，回弹的次数由曲线的周期决定，曲线的周期由 CycleInterpolator() 构造方法的参数决定。
         *
         * PathInterpolator: 自定义动画完成度 / 时间完成度曲线
         * 用这个 Interpolator 你可以定制出任何你想要的速度模型。定制的方式是使用一个 Path 对象来绘制出你要的动画完成度 / 时间完成度曲线。
         * 这条 Path 描述的其实是一个 y = f(x) (0 ≤ x ≤ 1) （y 为动画完成度，x 为时间完成度）的曲线，
         * 所以同一段时间完成度上不能有两段不同的动画完成度（因为内容不能出现分身术呀），
         * 而且每一个时间完成度的点上都必须要有对应的动画完成度（因为内容不能在某段时间段内消失呀）
         *
         * FastOutLinearInInterpolator: 加速运动
         * 它和 AccelerateInterpolator 一样，都是一个持续加速的运动路线。
         * 只不过 FastOutLinearInInterpolator 的曲线公式是用的贝塞尔曲线，而 AccelerateInterpolator 用的是指数曲线。
         * 具体来说，它俩最主要的区别是 FastOutLinearInInterpolator 的初始阶段加速度比 AccelerateInterpolator 要快一些。
         *
         * FastOutSlowInInterpolator: 先加速再减速
         * 同样也是先加速再减速的还有前面说过的 AccelerateDecelerateInterpolator，不过它们的效果是明显不一样的。
         * FastOutSlowInInterpolator 用的是贝塞尔曲线，AccelerateDecelerateInterpolator 用的是正弦 / 余弦曲线。
         * 具体来讲，FastOutSlowInInterpolator 的前期加速更猛一些，后期的减速过程的也减得更迅速
         *
         * LinearOutSlowInInterpolator: 持续减速
         * 它和 DecelerateInterpolator 比起来，同为减速曲线，主要区别在于 LinearOutSlowInInterpolator 的初始速度更高。
         */
        ViewPropertyAnimator viewPropertyAnimator = animate();
        switch (viewType) {
            case 0:
                viewPropertyAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                break;
            case 1:
                viewPropertyAnimator.setInterpolator(new LinearInterpolator());
                break;
            case 2:
                viewPropertyAnimator.setInterpolator(new AccelerateInterpolator());
                break;
            case 3:
                viewPropertyAnimator.setInterpolator(new DecelerateInterpolator());
                break;
            case 4:
                viewPropertyAnimator.setInterpolator(new AnticipateInterpolator());
                break;
            case 5:
                viewPropertyAnimator.setInterpolator(new OvershootInterpolator());
                break;
            case 6:
                viewPropertyAnimator.setInterpolator(new AnticipateOvershootInterpolator());
                break;
            case 7:
                viewPropertyAnimator.setInterpolator(new BounceInterpolator());
                break;
            case 8:
                viewPropertyAnimator.setInterpolator(new CycleInterpolator(0.5f));
                break;
            case 9:
                viewPropertyAnimator.setInterpolator(new CycleInterpolator(1.0f));
                break;
            case 10:
                viewPropertyAnimator.setInterpolator(new CycleInterpolator(1.5f));
                break;
            case 11:
                Path interpolatorPath = new Path();
                interpolatorPath.lineTo(1, 1);
                viewPropertyAnimator.setInterpolator(new PathInterpolator(interpolatorPath));
                break;
            case 12:
                Path interpolatorPath2 = new Path();
                interpolatorPath2.lineTo(0.1f, 0.0f);
                interpolatorPath2.lineTo(0.2f, 0.0f);
                interpolatorPath2.lineTo(0.3f, 0.2f);
                interpolatorPath2.lineTo(0.4f, 0.2f);
                interpolatorPath2.lineTo(0.5f, 0.4f);
                interpolatorPath2.lineTo(0.6f, 0.4f);
                interpolatorPath2.lineTo(0.7f, 0.6f);
                interpolatorPath2.lineTo(0.8f, 0.6f);
                interpolatorPath2.lineTo(0.9f, 0.8f);
                interpolatorPath2.lineTo(1, 1);
                viewPropertyAnimator.setInterpolator(new PathInterpolator(interpolatorPath2));
                break;
            case 13:
                viewPropertyAnimator.setInterpolator(new FastOutLinearInInterpolator());
                break;
            case 14:
                viewPropertyAnimator.setInterpolator(new FastOutSlowInInterpolator());
                break;
            case 15:
                viewPropertyAnimator.setInterpolator(new LinearOutSlowInInterpolator());
                break;
        }
        viewPropertyAnimator.translationX(300);
        viewPropertyAnimator.setDuration(800);
        postDelayed(() -> startAnim(), 2000);
    }
}
