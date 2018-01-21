package com.cashow.hencoderdemo.chapter_2_3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cashow.hencoderdemo.chapter_2_practice.NewTagGroup;
import com.cashow.hencoderdemo.common.BaseActivity;

import org.adrianwalker.multilinestring.Multiline;

import java.util.ArrayList;
import java.util.List;

public class Chapter_2_3_Activity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addMyDemoView(new View(context), INFO_0);
        addMyDemoView(new View(context), INFO_1);
        addMyDemoView(new View(context), INFO_2);
        addMyDemoView(new View(context), INFO_3);
        addMyDemoView(new View(context), INFO_4);
    }

    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{};
    }

    /**
     * Layout 内部布局的自定义
     * 1. 重写 onMeasure() 来计算内部布局；
     * 2. 重写 onLayout() 来摆放子 view。
     *
     * onMeasure() 的重写
     * 1. 调用每个子 view 的 measure()，让子 view 自我测量；
     * 2. 根据子 view 给出的尺寸，得出子 view 的位置，并保存他们的位置和尺寸；
     * 3. 根据子 view 的位置和尺寸计算出自己的尺寸，并用 setMeasuredDimension() 保存。
     */
    @Multiline
    static String INFO_0;

    /**
     * 第一步：调用每个子 view 的 measure()，让子 view 自我测量
     * 子 view 的 onMeasure() 方法里的两个参数 widthMeasureSpec 和 heightMeasureSpec，是父 view 传给子 view 的尺寸限制。
     * 这两个尺寸限制，是把开发者的要求（也就是布局文件里 layout_ 前缀的属性），和父 view 的剩余可用空间结合起来计算得到。
     * 对于每个子 view，查看他们的 layout_width 和 layout_height，分别用他们结合自己当前的可用宽度和可用高度，来计算出子 view 的限制。
     * 子 view 的 layout_width 和 layout_height，可通过子 view 的 getLayoutParams() 方法获得。
     *     LayoutParam 对象包含了布局文件里 layout_ 前缀的属性，其中 LayoutParam 的 width 和 height 分别对应了 layout_width 和 layout_height。
     *     布局文件里的属性对应的值：
     *     match_parent => LayoutParams.MATCH_PARENT
     *     wrap_content => LayoutParams.WRAP_CONTENT
     *     固定值，不管单位是 dp px 还是 sp => 转换成 px 值
     *
     * 以 layout_width 为例：
     * 1. 当 layout_width 是固定值时，那么不需要考虑可用空间问题，直接用 MeasureSpec.EXACTLY 限制子 view 的尺寸
     *    childWidthSpec = MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY);
     * 2. 当 layout_width 是 LayoutParams.MATCH_PARENT 时，子 view 需要填满父控件，这时将子 view 的宽度限制成固定值，这个固定值就是当前可用的宽度
     *    需要注意的是，对子 view 的测量，是发生在父 view 的 onMeasure() 方法里的，这个时候父 view 的尺寸还没有确定，只能通过 onMeasure() 的 widthMeasureSpec 和 heightMeasureSpec 得到一个可用空间。
     *    假如 widthMeasureSpec 的 mode 是 EXACTLY，那么可用空间是 widthMeasureSpec 的 size
     *    假如 widthMeasureSpec 的 mode 是 AT_MOST，那么可用空间应该是自己的尺寸上限，同样也是 widthMeasureSpec 的 size
     *    假如 widthMeasureSpec 的 mode 是 UNSPECIFIED，那么可用空间是无限的
     *
     * @Override
     * protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
     *     // 通过 widthMeasureSpec 和 heightMeasureSpec 可以获得父 view（即 SomeLayout）的可用空间
     *     int selfWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
     *     int selfWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
     *     int usedWidth = selfWidthSpecSize;
     *
     *     for (int i = 0; i < getChildCount(); i++) {
     *         View childView = getChildAt(i);
     *         int childWidthSpec;
     *
     *         LayoutParams layoutParams = childView.getLayoutParams();
     *         switch (layoutParams.width) {
     *             case LayoutParams.MATCH_PARENT:
     *                 // 填满父控件，这时将子 view 的宽度限制成固定值，这个固定值就是当前可用的宽度
     *                 if (selfWidthSpecMode == MeasureSpec.EXACTLY || selfWidthSpecMode == MeasureSpec.AT_MOST) {
     *                     childWidthSpec = MeasureSpec.makeMeasureSpec(selfWidthSpecSize - usedWidth, MeasureSpec.EXACTLY);
     *                 } else {
     *                     // 这个时候，子 view 的要求是填满父控件，但父控件的可用空间是无限大，那么直接把 UNSPECIFIED 传下去
     *                     // 这种情况下 size 没用，传 0 就行了
     *                     childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
     *                 }
     *                 break;
     *             case LayoutParams.WRAP_CONTENT:
     *                 // 让子 view 自己测量，但不能超过父 view 的边界
     *                 if (selfWidthSpecMode == MeasureSpec.EXACTLY || selfWidthSpecMode == MeasureSpec.AT_MOST) {
     *                     childWidthSpec = MeasureSpec.makeMeasureSpec(selfWidthSpecSize - usedWidth, MeasureSpec.AT_MOST);
     *                 } else {
     *                     childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
     *                 }
     *                 break;
     *             default:
     *                 // 如果子 view 的宽度是固定值，那么不需要考虑可用空间问题，直接用 MeasureSpec.EXACTLY 限制子 view 的尺寸
     *                 childWidthSpec = MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY);
     *                 break;
     *         }
     *     }
     * }
     */
    @Multiline
    static String INFO_1;

    /**
     * 第二步：根据子 view 给出的尺寸，得出子 view 的位置，并保存他们的位置和尺寸
     * 测量子 view 的过程，其实是一个排版过程。在测量过程中，能拿到每一个子 view 的位置，需要把他们保存下来。
     * 大部分情况下，view 测得的尺寸就是最终尺寸，在稍后要用的时候调用 getMeasuredWidth() 和 getMeasuredHeight() 就行。
     * 这些保存下来的位置和尺寸，在接下来的布局阶段，在 onLayout() 方法里，还要传给子 view。
     *
     * 关于保存子 view 位置的两点说明：
     * 1. 并不是所有的父 view 都要保存子 view 的位置。例如 LinearLayout，在布局阶段可以实时推导出子 view 的位置；
     * 2. 有时候对某些子 view 需要重复测量两次或多次才能得到正确的尺寸和位置。例如一个宽度是 wrap_content 的纵向 LinearLayout，
     *    而其中有 match_parent 的子 view 时，会先用自己的 widthMeasureSpec 去测量子 view，这个子 view 会得到他的宽度。
     *    在把所有的子 view 都测量完成后，LinearLayout会忽略掉 match_parent 的子 view，再测量一遍，
     *    这次测量时 mode 是 MeasureSpec.EXACTLY，size 是所有其他子 view 里面最宽的那个。
     */
    @Multiline
    static String INFO_2;

    /**
     * 第三步：根据子 view 的位置和尺寸计算出自己的尺寸，并用 setMeasuredDimension() 保存
     * 根据子 view 的边界计算出自己的尺寸并 setMeasuredDimension() 就行了。
     */
    @Multiline
    static String INFO_3;

    /**
     * 重写 onLayout()
     * 在 onLayout() 里面，你只需要做一件事，调用每一个子 view 的 layout() 方法，把之前在 onMeasure() 里保存下来的他们的位置和尺寸作为参数传进去，
     * 让他们把自己的位置和尺寸保存下来，并进行自我布局
     *
     * @Override
     * protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
     *     for (int i = 0; i < getChildCount(); i++) {
     *         View childView = getChildAt(i);
     *         childView.layout(childLeft[i], childTop[i], childRight[i], childBottom[i]);
     *     }
     * }
     */
    @Multiline
    static String INFO_4;

    /**
     * 继承自 ViewGroup 的标签组，实现了 onMeasure() 和 onLayout() 方法
     */
    @Multiline
    static String INFO_5;

    /**
     * 最大行数 : 1
     */
    @Multiline
    static String INFO_6;

    /**
     * 最大行数 : 2
     */
    @Multiline
    static String INFO_7;
}
