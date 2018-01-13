package com.cashow.hencoderdemo.chapter_1_6;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import com.cashow.hencoderdemo.R;
import com.cashow.hencoderdemo.common.MLog;

public class AnimatorListenerView extends ImageView {

    public AnimatorListenerView(Context context) {
        super(context);
        init();
    }

    public AnimatorListenerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatorListenerView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        /**
         * 设置监听器
         * ViewPropertyAnimator.setListener() / ObjectAnimator.addListener()
         * 这两个方法的名称不一样，可以设置的监听器数量也不一样，但它们的参数类型都是  AnimatorListener，所以本质上其实都是一样的。
         *
         * AnimatorListener 共有 4 个回调方法：
         * 1. onAnimationStart(Animator animation)
         * 当动画开始执行时，这个方法被调用。
         *
         * 2. onAnimationEnd(Animator animation)
         * 当动画结束时，这个方法被调用。
         *
         * 3. onAnimationCancel(Animator animation)
         * 当动画被通过 cancel() 方法取消时，这个方法被调用。
         * 就算动画被取消，onAnimationEnd() 也会被调用。
         * 所以当动画被取消时，如果设置了 AnimatorListener，那么 onAnimationCancel() 和 onAnimationEnd() 都会被调用。
         * onAnimationCancel() 会先于 onAnimationEnd() 被调用。
         *
         * 4. onAnimationRepeat(Animator animation)
         * 当动画通过 setRepeatMode() / setRepeatCount() 或 repeat() 方法重复执行时，这个方法被调用。
         *
         * 由于 ViewPropertyAnimator 不支持重复，所以这个方法对 ViewPropertyAnimator 相当于无效。
         *
         * 设置动画的属性更新的监听
         * ViewPropertyAnimator.setUpdateListener() / ObjectAnimator.addUpdateListener()
         * 当动画的属性更新时（不严谨的说，即每过 10 毫秒，动画的完成度更新时），这个方法被调用。
         * 方法的参数是一个 ValueAnimator，ValueAnimator 是 ObjectAnimator 的父类，也是 ViewPropertyAnimator 的内部实现，
         * 所以这个参数其实就是 ViewPropertyAnimator 内部的那个 ValueAnimator，或者对于 ObjectAnimator 来说就是它自己本身。
         *
         * ViewPropertyAnimator.withStartAction/EndAction()
         * 这两个方法是 ViewPropertyAnimator 的独有方法。它们和 set/addListener() 中回调的 onAnimationStart() / onAnimationEnd() 相比起来的不同主要有两点：
         * withStartAction() / withEndAction() 是一次性的，在动画执行结束后就自动弃掉了，就算之后再重用 ViewPropertyAnimator 来做别的动画，用它们设置的回调也不会再被调用。
         * 而 set/addListener() 所设置的 AnimatorListener 是持续有效的，当动画重复执行时，回调总会被调用。
         * withEndAction() 设置的回调只有在动画正常结束时才会被调用，而在动画被取消时不会被执行。这点和 AnimatorListener.onAnimationEnd() 的行为是不一致的。
         */
        setTranslationX(0);
        ViewPropertyAnimator viewPropertyAnimator = animate();
        viewPropertyAnimator.translationX(300);
        viewPropertyAnimator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                MLog.d("ViewPropertyAnimatorListenerView onAnimationCancel");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                MLog.d("ViewPropertyAnimatorListenerView onAnimationEnd");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                MLog.d("ViewPropertyAnimatorListenerView onAnimationRepeat");
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                MLog.d("ViewPropertyAnimatorListenerView onAnimationStart");
            }
        });
        viewPropertyAnimator.setUpdateListener(animation -> MLog.d("ViewPropertyAnimatorListenerView onAnimationUpdate"));
    }
}
