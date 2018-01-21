package com.cashow.hencoderdemo.chapter_2_1;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cashow.hencoderdemo.R;

public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * View 的 onMeasure() 方法测量到的结果尺寸，并不是作为返回值返回给父 view，
     * 而是通过 setMeasuredDimension() 方法将测量结果尺寸存在自己内部，
     * 这个尺寸可以通过 getMeasuredWidth() 和 getMeasuredHeight() 取到
     *
     * super.onMeasure() 方法在计算完成后，会调用 setMeasuredDimension() 方法，
     * 所以在 super.onMeasure() 之后可以通过 getMeasuredWidth() 和 getMeasuredHeight() 获取 super.onMeasure() 的测量结果
     *
     * 通过 setMeasuredDimension() 方法保存的这个尺寸，是一个测得的尺寸，相当于是 view 自己的期望尺寸，
     * 和之后通过 layout() 方法的参数传进来的尺寸未必是相等的
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 先执行原测量算法
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取原先的测量结果
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        measuredWidth = resolveSize(measuredWidth, widthMeasureSpec);
        measuredHeight = resolveSize(measuredHeight, heightMeasureSpec);

        // 利用原先的测量结果计算出新尺寸
        measuredWidth = measuredHeight = Math.min(measuredWidth, measuredHeight);

        // 保存计算后的结果
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    // 简化版的 resolveSize()
    public int mResolveSize(int size, int measureSpec) {
        // 限制的类型
        int specMode = MeasureSpec.getMode(measureSpec);
        // 限制的尺寸
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                // 无限制，直接返回计算结果
                return size;
            case MeasureSpec.AT_MOST:
                // 限制上限，返回 size 和 specSize 的最小值
                if (size <= specSize) {
                    return size;
                } else {
                    return specSize;
                }
            case MeasureSpec.EXACTLY:
                // 限制固定尺寸
                return specSize;

        }
        return size;
    }
}
