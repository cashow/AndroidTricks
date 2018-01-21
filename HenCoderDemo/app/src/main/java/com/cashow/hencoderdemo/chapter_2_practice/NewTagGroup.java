package com.cashow.hencoderdemo.chapter_2_practice;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cashow.hencoderdemo.R;

import java.util.List;

/**
 * 继承自 ViewGroup 的标签组，实现了 onMeasure() 和 onLayout() 方法
 */
public class NewTagGroup extends ViewGroup {
    // 标签列表
    private List<String> tagList;

    // 目前已占用的空间大小
    private int usedWidth;

    // 目前的行数
    private int lineNumber;

    // 最大行数。超过最大行数的标签不绘制。-1表示不限制最大行数
    private int maxLineNumber;

    // 标签在垂直方向的间距
    private int vertical_padding;
    // 标签在水平方向的间距
    private int horizontal_padding;

    // 控件的宽度
    private int viewWidth;

    // 标签的高度
    private int tagViewHeight;

    // 需要调用 layout() 方法的子 view 的个数。超过 maxLineNumber 的子 view 不需要调用 layout()。
    private int needLayoutCount;

    private int[] childLeft;
    private int[] childTop;
    private int[] childRight;
    private int[] childBottom;

    public static final int MAX_LINE_NO_LIMIT = -1;

    public NewTagGroup(Context context) {
        super(context);
        init();
    }

    public NewTagGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewTagGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        usedWidth = 0;
        lineNumber = 1;
        needLayoutCount = getChildCount();
        int selfWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int childHeightSpec = MeasureSpec.makeMeasureSpec(tagViewHeight, MeasureSpec.EXACTLY);

            childView.measure(childWidthSpec, childHeightSpec);

            if (usedWidth + childView.getMeasuredWidth() > selfWidthSpecSize) {
                // 如果子 view 的宽度小于可用宽度，换行
                usedWidth = 0;
                lineNumber += 1;
            }
            // 如果行数大于最大行数，停止测量
            if (maxLineNumber != MAX_LINE_NO_LIMIT && lineNumber > maxLineNumber) {
                needLayoutCount = i;
                break;
            }

            childLeft[i] = usedWidth;
            childTop[i] = tagViewHeight * (lineNumber - 1) + vertical_padding * (lineNumber - 1);
            childRight[i] = usedWidth + childView.getMeasuredWidth();
            childBottom[i] = childTop[i] + tagViewHeight;

            usedWidth = childRight[i] + horizontal_padding;
        }

        int selfHeightSpecMode = MeasureSpec.EXACTLY;
        int selfHeightSpecSize = getPaddingTop() + getPaddingBottom() + tagViewHeight * lineNumber + vertical_padding * (lineNumber - 1);
        int selfHeightSpec = MeasureSpec.makeMeasureSpec(selfHeightSpecSize, selfHeightSpecMode);
        setMeasuredDimension(widthMeasureSpec, selfHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < Math.min(getChildCount(), needLayoutCount); i++) {
            View childView = getChildAt(i);
            childView.layout(childLeft[i], childTop[i], childRight[i], childBottom[i]);
        }
    }

    private void init() {
        // 默认不限制最大行数
        maxLineNumber = MAX_LINE_NO_LIMIT;
        // 设置垂直方向的间距
        vertical_padding = dp2px(getContext(), 10);
        // 设置水平方向的间距
        horizontal_padding = dp2px(getContext(), 10);
        // 设置标签的高度
        tagViewHeight = dp2px(getContext(), 25);
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
        childLeft = new int[tagList.size()];
        childRight = new int[tagList.size()];
        childTop = new int[tagList.size()];
        childBottom = new int[tagList.size()];
        if (tagList != null && tagList.size() > 0) {
            for (String tag : tagList) {
                addTagView(tag);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != 0 && viewWidth != w) {
            // 控件宽度变化时需要重新绘制
            viewWidth = w;
            requestLayout();
        }
    }

    /**
     * 添加标签
     *
     * @param tag 要添加的标签
     * @return 添加成功返回true，添加失败返回false
     */
    private void addTagView(final String tag) {
        View tagView = LayoutInflater.from(getContext()).inflate(R.layout.layout_tag, this, false);

        TextView text = (TextView) tagView.findViewById(R.id.text);
        text.setText(tag);
        addView(tagView);
    }

    /**
     * 设置最大行数
     */
    public void setMaxLineNumber(int maxLineNumber) {
        this.maxLineNumber = maxLineNumber;
        requestLayout();
    }

    /**
     * 将dp转换成px值
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}