package com.cashow.taggroup;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 继承自 FrameLayout 的标签组，添加子 view 时需要计算子 view 的 x，y 坐标
 */
public class TagGroup extends FrameLayout {
    // 标签列表
    private List<MyTag> tagList;

    // 下一个标签的x坐标
    private int currentX;

    // 目前的行数
    private int lineNumber;

    // 最大行数。超过最大行数的标签不绘制。-1表示不限制最大行数
    private int maxLineNumber;

    // 标签的点击事件
    private OnTagClickListener onTagClickListener;

    // 标签在垂直方向的间距
    private int vertical_padding;
    // 标签在水平方向的间距
    private int horizontal_padding;

    // 控件的宽度
    private int viewWidth;
    // 控件的高度
    private int viewHeight;

    // 标签的高度
    private int tagViewHeight;

    // 是否已经添加标签
    private boolean isAddTagView;

    public static final int MAX_LINE_NO_LIMIT = -1;

    public TagGroup(Context context) {
        super(context);
        init();
    }

    public TagGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TagGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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

    public void setTagList(List<MyTag> tagList) {
        this.tagList = tagList;
        updateView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != 0 && viewWidth != w) {
            // 控件宽度变化时需要重新绘制
            viewWidth = w;
            isAddTagView = true;
            updateView();
        }
    }

    private void updateView() {
        post(new Runnable() {
            @Override
            public void run() {
                if (viewWidth == 0) {
                    return;
                }
                currentX = 0;
                lineNumber = 1;
                viewHeight = getPaddingTop() + getPaddingBottom() + tagViewHeight;
                removeAllViews();
                if (tagList != null && tagList.size() > 0) {
                    for (MyTag tag : tagList) {
                        if (!addTagView(tag)) {
                            // 如果添加标签失败，说明已达到最大行数限制，之后的标签也可以不添加了
                            break;
                        }
                    }
                }
            }
        });
    }

    /**
     * 添加标签
     * @param tag 要添加的标签
     * @return 添加成功返回true，添加失败返回false
     */
    private boolean addTagView(final MyTag tag) {
        View tagView = LayoutInflater.from(getContext()).inflate(R.layout.layout_collocation_detail_tag_view, this, false);

        TextView text = (TextView) tagView.findViewById(R.id.text);
        text.setText(tag.name);

        tagView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTagClickListener != null) {
                    onTagClickListener.onTagClick(tag);
                }
            }
        });

        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        tagView.measure(width, height);
        // 计算要添加的表情的宽度
        int measuredWidth = tagView.getMeasuredWidth();

        // 如果这个标签的宽度大于当前行剩余的宽度，需要将这个标签添加到下一行
        if (measuredWidth + currentX > viewWidth - getPaddingLeft() - getPaddingRight()) {
            currentX = 0;
            lineNumber += 1;
            // 如果不限制行数，或者目前的行数小于最大行数，那么添加新的行
            if (maxLineNumber == MAX_LINE_NO_LIMIT || lineNumber <= maxLineNumber) {
                viewHeight += tagViewHeight + vertical_padding;

                ViewGroup.LayoutParams params = getLayoutParams();
                if (params == null) {
                    params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewHeight);
                } else {
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = viewHeight;
                }
                setLayoutParams(params);
            }
        }
        // 如果超出了最大行数，不添加新的标签进来
        if (maxLineNumber != MAX_LINE_NO_LIMIT && lineNumber > maxLineNumber) {
            return false;
        }

        LayoutParams params = new LayoutParams(measuredWidth, tagViewHeight);
        params.width = measuredWidth;
        params.height = tagViewHeight;

        int currentY = (lineNumber - 1) * tagViewHeight;
        if (lineNumber >= 2) {
            currentY += vertical_padding * (lineNumber - 1);
        }
        tagView.setX(currentX);
        tagView.setY(currentY);
        tagView.setLayoutParams(params);

        currentX += measuredWidth + horizontal_padding;

        addView(tagView);
        return true;
    }

    /**
     * 设置标签点击的回调
     */
    public void setOnTagClickListener(OnTagClickListener clickListener) {
        this.onTagClickListener = clickListener;
    }

    /**
     * 设置最大行数
     */
    public void setMaxLineNumber(int maxLineNumber) {
        this.maxLineNumber = maxLineNumber;
        // 如果在设置最大行数之前已经进行绘制了，需要重新绘制一遍
        if (isAddTagView) {
            updateView();
        }
    }

    /**
     * 将dp转换成px值
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}