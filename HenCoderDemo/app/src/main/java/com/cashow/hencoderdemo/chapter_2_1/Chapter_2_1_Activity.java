package com.cashow.hencoderdemo.chapter_2_1;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cashow.hencoderdemo.common.BaseActivity;

import org.adrianwalker.multilinestring.Multiline;

public class Chapter_2_1_Activity extends BaseActivity {
    /**
     * 布局过程，就是程序在运行时利用布局文件的代码来计算出实际尺寸的过程。
     * 布局过程的工作内容分两个阶段：
     * 测量阶段：从上到下递归地调用每个 View 或者 ViewGroup 的 measure() 方法，测量他们的尺寸并计算它们的位置；
     * 布局阶段：从上到下递归地调用每个 View 或者 ViewGroup 的 layout() 方法，把测得的它们的尺寸和位置赋值给它们。
     *
     * View 或 ViewGroup 的布局过程：
     * 1. 测量阶段，measure() 方法被父 View 调用，在 measure() 中做一些准备和优化工作后，调用 onMeasure() 来进行实际的自我测量。
     * onMeasure() 做的事，View 和 ViewGroup 不一样：
     *   View：View 在 onMeasure() 中会计算出自己的尺寸然后保存；
     *   ViewGroup：ViewGroup 在 onMeasure() 中会调用所有子 View 的 measure() 让它们进行自我测量，
     *     并根据子 View 计算出的期望尺寸来计算出它们的实际尺寸和位置
     *     （实际上 99.99% 的父 View 都会使用子 View 给出的期望尺寸来作为实际尺寸）然后保存。
     *     同时，它也会根据子 View 的尺寸和位置来计算出自己的尺寸然后保存；
     *
     * 2. 布局阶段，layout() 方法被父 View 调用，在 layout() 中它会保存父 View 传进来的自己的位置和尺寸，并且调用 onLayout() 来进行实际的内部布局。
     * onLayout() 做的事， View 和 ViewGroup 也不一样：
     *   View：由于没有子 View，所以 View 的 onLayout() 什么也不做。
     *   ViewGroup：ViewGroup 在 onLayout() 中会调用自己的所有子 View 的 layout() 方法，把它们的尺寸和位置传给它们，让它们完成自我的内部布局。
     *
     * 布局过程自定义的方式：
     * 重写 onMeasure() 来修改已有的 View 的尺寸；
     * 重写 onMeasure() 来全新定制自定义 View 的尺寸；
     * 重写 onMeasure() 和 onLayout() 来全新定制自定义 ViewGroup 的内部布局。
     *
     * 重写 onMeasure() 来修改已有的 View 的尺寸的具体做法：
     * 重写 onMeasure() 方法，并在里面调用 super.onMeasure()，触发原有的自我测量；
     * 在 super.onMeasure() 的下面用 getMeasuredWidth() 和 getMeasuredHeight() 来获取到之前的测量结果，并使用自己的算法，根据测量结果计算出新的结果；
     * 调用 setMeasuredDimension() 来保存新的结果。
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addMyDemoView(new SquareImageParentView(context), INFO_0);
    }

    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{};
    }

    /**
     * 布局过程，就是程序在运行时利用布局文件的代码来计算出实际尺寸的过程。
     * 布局过程的工作内容分两个阶段：
     * 测量阶段：从上到下递归地调用每个 View 或者 ViewGroup 的 measure() 方法，测量他们的尺寸并计算它们的位置；
     * 布局阶段：从上到下递归地调用每个 View 或者 ViewGroup 的 layout() 方法，把测得的它们的尺寸和位置赋值给它们。
     *
     * View 或 ViewGroup 的布局过程：
     * 1. 测量阶段，measure() 方法被父 View 调用，在 measure() 中做一些准备和优化工作后，调用 onMeasure() 来进行实际的自我测量。
     * onMeasure() 做的事，View 和 ViewGroup 不一样：
     * View：View 在 onMeasure() 中会计算出自己的尺寸然后保存；
     * ViewGroup：ViewGroup 在 onMeasure() 中会调用所有子 View 的 measure() 让它们进行自我测量，并根据子 View 计算出的期望尺寸来计算出它们的实际尺寸和位置（实际上 99.99% 的父 View 都会使用子 View 给出的期望尺寸来作为实际尺寸）然后保存。
     * 同时，它也会根据子 View 的尺寸和位置来计算出自己的尺寸然后保存；
     *
     * 2. 布局阶段，layout() 方法被父 View 调用，在 layout() 中它会保存父 View 传进来的自己的位置和尺寸，并且调用 onLayout() 来进行实际的内部布局。
     * onLayout() 做的事， View 和 ViewGroup 也不一样：
     * View：由于没有子 View，所以 View 的 onLayout() 什么也不做。
     * ViewGroup：ViewGroup 在 onLayout() 中会调用自己的所有子 View 的 layout() 方法，把它们的尺寸和位置传给它们，让它们完成自我的内部布局。
     *
     * 布局过程自定义的方式：
     * 重写 onMeasure() 来修改已有的 View 的尺寸；
     * 重写 onMeasure() 来全新定制自定义 View 的尺寸；
     * 重写 onMeasure() 和 onLayout() 来全新定制自定义 ViewGroup 的内部布局。
     *
     * 重写 onMeasure() 来修改已有的 View 的尺寸的具体做法：
     * 重写 onMeasure() 方法，并在里面调用 super.onMeasure()，触发原有的自我测量；
     * 在 super.onMeasure() 的下面用 getMeasuredWidth() 和 getMeasuredHeight() 来获取到之前的测量结果，并使用自己的算法，根据测量结果计算出新的结果；
     * 调用 setMeasuredDimension() 来保存新的结果。
     *
     *
     * View 的 onMeasure() 方法测量到的结果尺寸，并不是作为返回值返回给父 view，而是通过 setMeasuredDimension() 方法将测量结果尺寸存在自己内部，这个尺寸可以通过 getMeasuredWidth() 和 getMeasuredHeight() 取到。
     * super.onMeasure() 方法在计算完成后，会调用 setMeasuredDimension() 方法，所以在 super.onMeasure() 之后可以通过 getMeasuredWidth() 和 getMeasuredHeight() 获取 super.onMeasure() 的测量结果
     * 通过 setMeasuredDimension() 方法保存的这个尺寸，是一个测得的尺寸，相当于是 view 自己的期望尺寸，和之后通过 layout() 方法的参数传进来的尺寸未必是相等的。
     *
     * public class SquareImageView extends ImageView {
     *     @Override
     *     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
     *         // 先执行原测量算法
     *         super.onMeasure(widthMeasureSpec, heightMeasureSpec);
     *
     *         // 获取原先的测量结果
     *         int measuredWidth = getMeasuredWidth();
     *         int measuredHeight = getMeasuredHeight();
     *
     *         // 利用原先的测量结果计算出新尺寸
     *         measuredWidth = measuredHeight = Math.min(measuredWidth, measuredHeight);
     *
     *         // 保存计算后的结果
     *         setMeasuredDimension(measuredWidth, measuredHeight);
     *     }
     * }
     */
    @Multiline
    static String INFO_0;
}
