package com.cashow.hencoderdemo.chapter_1_5;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cashow.hencoderdemo.common.BaseActivity;

public class Chapter_1_5_Activity extends BaseActivity {
    /**
     * 具体来讲，一个完整的绘制过程会依次绘制以下几个内容：
     * 1. 背景
     * 2. 主体（onDraw()）
     * 3. 子 View（dispatchDraw()）
     * 4. 滑动边缘渐变和滑动条
     * 5. 前景
     *
     * 背景的绘制发生在一个叫 drawBackground() 的方法里，但这个方法是 private 的，不能重写，
     * 你如果要设置背景，只能用自带的 API 去设置（xml 布局文件的 android:background 属性，
     * 以及 Java 代码的 View.setBackgroundXxx() 方法），而不能自定义绘制
     *
     * 在 View 这个类里，onDraw() 是空实现，绘制代码写在 super.onDraw() 的上面还是下面都无所谓
     * 当继承一个具有某种功能的控件，需要根据自己的需求，
     * 判断出你绘制的内容需要盖住控件原有的内容还是需要被控件原有的内容盖住，
     * 从而确定你的绘制代码是应该写在 super.onDraw() 的上面还是下面。
     *
     * 在绘制过程中，每个 View 和 ViewGroup 都会先调用 onDraw() 方法来绘制主体，再调用 dispatchDraw() 方法来绘制子 View。
     * 只要重写 dispatchDraw()，并在 super.dispatchDraw() 的下面写上你的绘制代码，这段绘制代码就会发生在子 View 的绘制之后，从而让绘制内容盖住子 View 了。
     * 同理，把绘制代码写在 super.dispatchDraw() 的上面，这段绘制就会在 onDraw() 之后、super.dispatchDraw() 之前发生，也就是绘制内容会出现在主体内容和子 View 之间。
     *
     * 第 4、5 两步——滑动边缘渐变和滑动条以及前景，这两部分被合在一起放在了 onDrawForeground() 方法里，这个方法是可以重写的。
     * 前景的支持是在 Android 6.0（也就是 API 23）才加入的；之前其实也有，不过只支持 FrameLayout，而直到 6.0 才把这个支持放进了 View 类里。
     * 滑动边缘渐变和滑动条可以通过 xml 的 android:scrollbarXXX 系列属性或 Java 代码的  View.setXXXScrollbarXXX() 系列方法来设置；
     * 前景可以通过 xml 的 android:foreground 属性或 Java 代码的 View.setForeground() 方法来设置。
     * 而重写 onDrawForeground() 方法，并在它的  super.onDrawForeground() 方法的上面或下面插入绘制代码，则可以控制绘制内容和滑动边缘渐变、滑动条以及前景的遮盖关系。
     *
     * 除了 onDraw() dispatchDraw() 和 onDrawForeground() 之外，还有一个可以用来实现自定义绘制的方法： draw()。
     * draw() 是绘制过程的总调度方法。一个 View 的整个绘制过程都发生在 draw() 方法里。前面讲到的背景、主体、子 View 、滑动相关以及前景的绘制，它们其实都是在 draw() 方法里的。
     * 由于 draw() 是总调度方法，所以如果把绘制代码写在 super.draw() 的下面，那么这段代码会在其他所有绘制完成之后再执行，也就是说，它的绘制内容会盖住其他的所有绘制内容。
     * 它的效果和重写 onDrawForeground()，并把绘制代码写在 super.onDrawForeground() 下面时的效果是一样的：都会盖住其他的所有内容。
     * 同理，由于 draw() 是总调度方法，所以如果把绘制代码写在 super.draw() 的上面，那么这段代码会在其他所有绘制之前被执行，所以这部分绘制内容会被其他所有的内容盖住，包括背景。是的，背景也会盖住它。
     *
     * 关于绘制方法，有两点需要注意一下：
     * 出于效率的考虑，ViewGroup 默认会绕过 draw() 方法，换而直接执行 dispatchDraw()，以此来简化绘制流程。
     * 所以如果你自定义了某个 ViewGroup 的子类（比如 LinearLayout）并且需要在它的除  dispatchDraw() 以外的任何一个绘制方法内绘制内容，
     * 你可能会需要调用  View.setWillNotDraw(false) 这行代码来切换到完整的绘制流程（是「可能」而不是「必须」的原因是，有些 ViewGroup 是已经调用过 setWillNotDraw(false) 了的，例如 ScrollView）。
     * 有的时候，一段绘制代码写在不同的绘制方法中效果是一样的，这时你可以选一个自己喜欢或者习惯的绘制方法来重写。
     * 但有一个例外：如果绘制代码既可以写在 onDraw() 里，也可以写在其他绘制方法里，那么优先写在 onDraw() ，
     * 因为 Android 有相关的优化，可以在不需要重绘的时候自动跳过  onDraw() 的重复执行，以提升开发效率。享受这种优化的只有 onDraw() 一个方法。
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addView(new AfterOnDrawView(context));
        addView(new BeforeOnDrawView(context));
        addView(new BeforeDispatchDrawView(context));
        addView(new AfterDispatchDrawView(context));
        addView(new OnDrawForegroundView(context));
        addView(new BeforeOnDrawForegroundView(context));
        addView(new AfterOnDrawForegroundView(context));
        addView(new BeforeDrawView(context));
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
