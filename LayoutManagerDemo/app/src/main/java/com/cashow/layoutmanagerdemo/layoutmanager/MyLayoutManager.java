package com.cashow.layoutmanagerdemo.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

/**
 * 只实现了简单的 onLayoutChildren() 的竖向 LayoutManager，没有实现 view 的回收，不可滑动
 * 页面加载后 adapter 会调用 2*50 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 50 个
 */
public class MyLayoutManager extends LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 在布局之前，将所有的子 View 先 Detach 掉，放入到 Scrap 缓存中
        detachAndScrapAttachedViews(recycler);

        // 定义竖直方向的偏移量
        int offsetY = 0;
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
        }
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }
}
