package com.cashow.hencoderdemo.chapter_1_6;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cashow.hencoderdemo.common.BaseActivity;

import org.adrianwalker.multilinestring.Multiline;

import java.lang.reflect.Field;

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

        addMyDemoView(new TranslationXView(context), INFO_0);
        addMyDemoView(new RotationView(context), INFO_1);
        addMyDemoView(new ScaleXView(context), INFO_2);
        addMyDemoView(new ObjectAnimatorView(context), INFO_3);
        addMyDemoView(new Duration300View(context), INFO_4);
        addMyDemoView(new Duration600View(context), INFO_5);
        addDivider();
        for (int i = 0; i <= 15; i++) {
            View view = new InterpolatorView(context, i);
            Class clz = view.getClass();
            Field field = null;
            String info = "";
            try {
                field = clz.getDeclaredField("INFO_" + i);
                info = field.get(view).toString();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            addMyDemoView(view, info);
        }
        addMyDemoView(new AnimatorListenerView(context), INFO_6);
    }

    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{};
    }

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
     *
     * ViewPropertyAnimator viewPropertyAnimator = animate();
     * viewPropertyAnimator.translationX(300);
     */
    @Multiline
    static String INFO_0;

    /**
     * ViewPropertyAnimator viewPropertyAnimator = animate();
     * viewPropertyAnimator.rotation(360);
     */
    @Multiline
    static String INFO_1;

    /**
     * ViewPropertyAnimator viewPropertyAnimator = animate();
     * viewPropertyAnimator.scaleX(2.0f);
     */
    @Multiline
    static String INFO_2;

    /**
     * public class ObjectAnimatorView extends View {
     *     private Paint paint;
     *     float progress = 0;
     *
     *     ...
     *
     *     private void init() {
     *         paint = new Paint();
     *         paint.setColor(Color.parseColor("#e33b63"));
     *         paint.setStyle(Paint.Style.FILL);
     *
     *         ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(300, 300);
     *         setLayoutParams(params);
     *
     *         post(() -> startAnim());
     *     }
     *
     *     public float getProgress() {
     *         return progress;
     *     }
     *
     *     public void setProgress(float progress) {
     *         this.progress = progress;
     *         invalidate();
     *     }
     *
     *     private void startAnim() {
     *         setProgress(0);
     *         ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", 0, 100);
     *         objectAnimator.start();
     *         objectAnimator.addListener(new AnimatorListenerAdapter() {
     *             @Override
     *             public void onAnimationEnd(Animator animation) {
     *                 super.onAnimationEnd(animation);
     *                 postDelayed(() -> startAnim(), 1000);
     *             }
     *         });
     *     }
     *
     *     @Override
     *     protected void onDraw(Canvas canvas) {
     *         super.onDraw(canvas);
     *         canvas.drawArc(0, 0, 200, 200, -110, progress, true, paint);
     *     }
     * }
     */
    @Multiline
    static String INFO_3;

    /**
     * ViewPropertyAnimator viewPropertyAnimator = animate();
     * viewPropertyAnimator.setDuration(300);
     * viewPropertyAnimator.translationX(300);
     */
    @Multiline
    static String INFO_4;

    /**
     * ViewPropertyAnimator viewPropertyAnimator = animate();
     * viewPropertyAnimator.setDuration(600);
     * viewPropertyAnimator.translationX(300);
     */
    @Multiline
    static String INFO_5;

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
    @Multiline
    static String INFO_6;
}
