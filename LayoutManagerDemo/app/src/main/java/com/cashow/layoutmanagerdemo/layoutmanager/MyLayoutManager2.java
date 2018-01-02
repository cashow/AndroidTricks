package com.cashow.layoutmanagerdemo.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

/**
 * 只实现了简单的 onLayoutChildren() 和 scrollVerticallyBy() 的竖向 LayoutManager，没有实现 view 的回收，可以竖向滑动
 * 页面加载后 adapter 会调用 2*50 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 50 个
 * 在滑动中获取到的 RecyclerView 的子 view 数也是 50 个
 */
public class MyLayoutManager2 extends LayoutManager {
    // 垂直方向的滑动偏移量
    private int verticalScrollOffset;
    // 所有子 view 的高度之和
    private int totalHeight;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0) {
            // 如果没有item，直接返回
            return;
        }
        if (state.isPreLayout()) {
            // 跳过 preLayout。preLayout 主要用于支持动画
            return;
        }
        // 在布局之前，将所有的子 View 先 Detach 掉，放入到 Scrap 缓存中
        detachAndScrapAttachedViews(recycler);

        // 定义竖直方向的偏移量
        int offsetY = 0;
        totalHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            // 这里就是从缓存里面取出
            View view = recycler.getViewForPosition(i);
            // 将 View 加入到 RecyclerView 中
            addView(view);
            // 对子 View 进行测量
            measureChildWithMargins(view, 0, 0);
            // 把宽高拿到，宽高都是包含 ItemDecorate 的尺寸
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);
            // 最后，将 View 布局
            layoutDecorated(view, 0, offsetY, width, offsetY + height);
            // 将竖直方向偏移量增大 height
            offsetY += height;
            totalHeight += height;
        }
        // 如果所有子 View 的高度和没有填满 RecyclerView 的高度，
        // 则将高度设置为 RecyclerView 的高度
        totalHeight = Math.max(totalHeight, getVerticalSpace());
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 实际要滑动的距离
        int travel = dy;

        // 如果滑动到最顶部
        if (verticalScrollOffset + dy < 0) {
            travel -= verticalScrollOffset;
        } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {
            //如果滑动到最底部
            travel = totalHeight - getVerticalSpace() - verticalScrollOffset;
        }

        // 将竖直方向的偏移量加上 travel
        verticalScrollOffset += travel;

        // 平移容器内的 item
        offsetChildrenVertical(-travel);

        return travel;
    }

    /**
     * 获取 RecyclerView 在垂直方向上的可用空间
     */
    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }
}
