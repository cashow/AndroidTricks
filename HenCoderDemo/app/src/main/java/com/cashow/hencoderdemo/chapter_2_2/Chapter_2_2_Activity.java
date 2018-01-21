package com.cashow.hencoderdemo.chapter_2_2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cashow.hencoderdemo.common.BaseActivity;

import org.adrianwalker.multilinestring.Multiline;

public class Chapter_2_2_Activity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addMyDemoView(new View(context), INFO_0);
    }

    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{};
    }

    /**
     * 定制 Layout 内部布局的方式
     * 1. 重写 onMeasure() 来计算内部布局；
     * 2. 重写 onLayout() 来摆放子 View。
     *
     * 重写 onMeasure() 的三个步骤：
     * 1. 调用每个子 View 的 measure() 来计算子 View 的尺寸；
     * 2. 计算子 View 的位置并保存子 View 的位置和尺寸；
     * 3. 计算自己的尺寸并用 setMeasuredDimension() 保存。
     *
     * 计算子 View 尺寸的关键
     * 计算子 View 的尺寸，关键在于 measure() 方法的两个参数——也就是子 View 的两个 MeasureSpec 的计算。
     * 子 View 的 MeasureSpec 的计算方式：
     * 1. 结合开发者的要求（xml 中 layout_ 打头的属性）和自己的可用空间（自己的尺寸上限 - 已用尺寸）；
     * 2. 尺寸上限根据自己的 MeasureSpec 中的 mode 而定：
     *    EXACTLY / AT_MOST：尺寸上限为 MeasureSpec 中的 size
     *    UNSPECIFIED：尺寸无上限
     *
     * 重写 onLayout() 的方式
     * 在 onLayout() 里调用每个子 View 的 layout() ，让它们保存自己的位置和尺寸。
     *
     * 如果要全新定义自定义 view 的尺寸，在重写 onMeasure()，可以不调用 super.onMeasure()，因为这个测量过程是要自己实现的，父类也帮不了你。
     * 父 view 的限制是 onMeasure(int widthMeasureSpec, int heightMeasureSpec) 方法里的 widthMeasureSpec 和 heightMeasureSpec，
     * 这两个参数是父 view 在调用子 view 的 measure() 方法时传给子 view 的，子 view 在计算自己尺寸时，需要遵守这两个参数所包含的限制。
     * 布局文件里，layout_ 前缀的属性，是给父 view 看的，父 view 经过处理之后，通过 widthMeasureSpec 和 heightMeasureSpec 去限制子 view 的尺寸。
     * 子 view 如果要遵守父 view 的限制，需要调用一个方法 resolveSize()，在你计算完 measuredWidth 和 measuredHeight 后，分别调用一次 resolveSize()，返回的结果就是符合父 view 限制的修正之后的尺寸。
     *
     * measuredWidth = resolveSize(measuredWidth, widthMeasureSpec);
     * measuredHeight = resolveSize(measuredHeight, heightMeasureSpec);
     * setMeasuredDimension(measuredWidth, measuredHeight);
     *
     * 总结起来，全新定义 view 尺寸的方式：
     * 1. 重写 onMeasure() 把尺寸计算出来
     * 2. 把计算的结果用 resolveSize() 过滤一遍后保存
     *
     * widthMeasureSpec 和 heightMeasureSpec 虽然是 int 类型的，但其实是个压缩数据，这个数据里包含了这个限制的类型以及这个限制的尺寸值。
     *
     * // 简化版的 resolveSize()
     * public int mResolveSize(int size, int measureSpec) {
     *     // 限制的类型
     *     int specMode = MeasureSpec.getMode(measureSpec);
     *     // 限制的尺寸
     *     int specSize = MeasureSpec.getSize(measureSpec);
     *
     *     switch (specMode) {
     *         case MeasureSpec.UNSPECIFIED:
     *             // 无限制，直接返回计算结果
     *             return size;
     *         case MeasureSpec.AT_MOST:
     *             // 限制上限，返回 size 和 specSize 的最小值
     *             if (size <= specSize) {
     *                 return size;
     *             } else {
     *                 return specSize;
     *             }
     *         case MeasureSpec.EXACTLY:
     *             // 限制固定尺寸
     *             return specSize;
     *
     *     }
     *     return size;
     * }
     */
    @Multiline
    static String INFO_0;
}
