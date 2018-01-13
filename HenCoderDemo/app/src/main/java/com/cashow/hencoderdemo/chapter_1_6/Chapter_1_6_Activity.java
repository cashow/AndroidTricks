package com.cashow.hencoderdemo.chapter_1_6;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cashow.hencoderdemo.common.BaseActivity;

public class Chapter_1_6_Activity extends BaseActivity {
    /**
     * ViewPropertyAnimator
     * 使用方式：View.animate() 后跟 translationX() 等方法，动画会自动执行。
     *
     * View 的每个方法都对应了 ViewPropertyAnimator 的两个方法，其中一个是带有  -By 后缀的，
     * 例如，View.setTranslationX() 对应了 ViewPropertyAnimator.translationX() 和  ViewPropertyAnimator.translationXBy() 这两个方法。
     * 其中带有 -By() 后缀的是增量版本的方法，例如，translationX(100) 表示用动画把 View 的 translationX 值渐变为 100，
     * 而 translationXBy(100) 则表示用动画把 View 的 translationX 值渐变地增加 100。
     *
     * ObjectAnimator
     * 使用方式：
     * 1. 如果是自定义控件，需要添加 setter / getter 方法；
     * 2. 用 ObjectAnimator.ofXXX() 创建 ObjectAnimator 对象；
     * 3. 用 start() 方法执行动画。
     *
     *
     * 通用功能:
     *
     * 设置动画时长
     * setDuration(int duration)
     * 单位是毫秒
     *
     * 设置速度设置器
     * setInterpolator(Interpolator interpolator)
     *
     * 设置监听器
     * 设置监听器的方法， ViewPropertyAnimator 和 ObjectAnimator 略微不一样：
     * ViewPropertyAnimator 用的是 setListener() 和 setUpdateListener() 方法，可以设置一个监听器，
     * 要移除监听器时通过 set[Update]Listener(null) 填 null 值来移除；
     * 而 ObjectAnimator 则是用 addListener() 和 addUpdateListener() 来添加一个或多个监听器，
     * 移除监听器则是通过 remove[Update]Listener() 来指定移除对象。
     * 另外，由于 ObjectAnimator 支持使用 pause() 方法暂停，所以它还多了一个 addPauseListener() / removePauseListener() 的支持；
     * 而 ViewPropertyAnimator 则独有 withStartAction() 和 withEndAction() 方法，可以设置一次性的动画开始或结束的监听。
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutContainer.addView(new TranslationXView(context));
        layoutContainer.addView(new RotationView(context));
        layoutContainer.addView(new ScaleXView(context));
        layoutContainer.addView(new ObjectAnimatorView(context));
        layoutContainer.addView(new Duration300View(context));
        layoutContainer.addView(new Duration600View(context));
        addDivider();
        for (int i = 0; i <= 15; i++) {
            layoutContainer.addView(new InterpolatorView(context, i));
        }
        layoutContainer.addView(new AnimatorListenerView(context));
    }

    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{};
    }

    @Override
    protected boolean isAutoAddDemoView() {
        return false;
    }
}
