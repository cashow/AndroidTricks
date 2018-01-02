package com.cashow.layoutmanagerdemo.layoutmanager;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;

/**
 * 只实现了简单的 onLayoutChildren() 和 scrollVerticallyBy() 的竖向 LayoutManager，实现了 view 的回收，可以竖向滑动
 * 页面加载后 adapter 会先调用 50 次 onCreateViewHolder() 和 onBindViewHolder()，然后调用 8 次 onCreateViewHolder() 和 13 次 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 38 个
 * 在滑动中获取到的 RecyclerView 的子 view 数是 13、14 个
 */
public class MyLayoutManager3 extends LayoutManager {
    // 垂直方向的滑动偏移量
    private int verticalScrollOffset;
    // 所有子 view 的高度之和
    private int totalHeight;

    // 保存所有的 view 的上下左右的偏移信息
    private SparseArray<Rect> allItemFrames = new SparseArray<>();
    // 记录 view 是否出现过屏幕并且还没有回收
    private SparseBooleanArray hasAttachedItems = new SparseBooleanArray();

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
            // 将竖直方向偏移量增大 height
            totalHeight += height;

            Rect frame = allItemFrames.get(i);
            if (frame == null) {
                frame = new Rect();
            }
            frame.set(0, offsetY, width, offsetY + height);
            // 将当前 view 的 Rect 边界数据保存
            allItemFrames.put(i, frame);
            // 由于已经调用了detachAndScrapAttachedViews，因此需要将当前的Item设置为未出现过
            hasAttachedItems.put(i, false);
            offsetY += height;
        }
        // 如果所有子 View 的高度和没有填满 RecyclerView 的高度，
        // 则将高度设置为 RecyclerView 的高度
        totalHeight = Math.max(totalHeight, getVerticalSpace());
        recycleAndFillItems(recycler, state);
    }

    /**
     * 回收不需要的 view，并且将需要显示的 view 从缓存中取出
     */
    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            // 跳过 preLayout。preLayout 主要用于支持动画
            return;
        }
        // 当前scroll offset状态下的显示区域
        Rect displayFrame = new Rect(0, verticalScrollOffset, getHorizontalSpace(), verticalScrollOffset + getVerticalSpace());

        /**
         * 将滑出屏幕的 view 回收到 Recycler 中
         */
        Rect childFrame = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childFrame.left = getDecoratedLeft(child);
            childFrame.top = getDecoratedTop(child);
            childFrame.right = getDecoratedRight(child);
            childFrame.bottom = getDecoratedBottom(child);
            // 如果 view 没有在显示区域，说明需要回收
            if (!Rect.intersects(displayFrame, childFrame)) {
                // 回收掉滑出屏幕的 view
                removeAndRecycleView(child, recycler);
            }
        }

        // 重新显示需要出现在屏幕中的 view
        for (int i = 0; i < getItemCount(); i++) {
            if (Rect.intersects(displayFrame, allItemFrames.get(i))) {
                View scrap = recycler.getViewForPosition(i);
                measureChildWithMargins(scrap, 0, 0);
                addView(scrap);

                Rect frame = allItemFrames.get(i);
                // 将这个 item 布局出来
                layoutDecorated(scrap, frame.left, frame.top - verticalScrollOffset, frame.right, frame.bottom - verticalScrollOffset);
            }
        }
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 先 detach 掉所有的子 view
        detachAndScrapAttachedViews(recycler);

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

        recycleAndFillItems(recycler, state);

        return travel;
    }

    /**
     * 获取 RecyclerView 在垂直方向上的可用空间
     */
    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    /**
     * 获取 RecyclerView 在水平方向上的可用空间
     */
    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
}
