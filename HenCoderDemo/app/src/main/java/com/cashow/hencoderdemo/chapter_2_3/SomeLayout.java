package com.cashow.hencoderdemo.chapter_2_3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class SomeLayout extends ViewGroup {
    public SomeLayout(Context context) {
        super(context);
    }

    public SomeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SomeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 通过 widthMeasureSpec 和 heightMeasureSpec 可以获得父 view（即 SomeLayout）的可用空间
        int selfWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int selfWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int usedWidth = selfWidthSpecSize;

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int childWidthSpec;

            LayoutParams layoutParams = childView.getLayoutParams();
            switch (layoutParams.width) {
                case LayoutParams.MATCH_PARENT:
                    // 填满父控件，这时将子 view 的宽度限制成固定值，这个固定值就是当前可用的宽度
                    if (selfWidthSpecMode == MeasureSpec.EXACTLY || selfWidthSpecMode == MeasureSpec.AT_MOST) {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(selfWidthSpecSize - usedWidth, MeasureSpec.EXACTLY);
                    } else {
                        // 这个时候，子 view 的要求是填满父控件，但父控件的可用空间是无限大，那么直接把 UNSPECIFIED 传下去
                        // 这种情况下 size 没用，传 0 就行了
                        childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    }
                    break;
                case LayoutParams.WRAP_CONTENT:
                    // 让子 view 自己测量，但不能超过父 view 的边界
                    if (selfWidthSpecMode == MeasureSpec.EXACTLY || selfWidthSpecMode == MeasureSpec.AT_MOST) {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(selfWidthSpecSize - usedWidth, MeasureSpec.AT_MOST);
                    } else {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    }
                    break;
                default:
                    // 如果子 view 的宽度是固定值，那么不需要考虑可用空间问题，直接用 MeasureSpec.EXACTLY 限制子 view 的尺寸
                    childWidthSpec = MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY);
                    break;
            }
        }
    }
}
