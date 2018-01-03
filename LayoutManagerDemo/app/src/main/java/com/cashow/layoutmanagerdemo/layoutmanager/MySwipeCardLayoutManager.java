package com.cashow.layoutmanagerdemo.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cashow.layoutmanagerdemo.swipecard.CardConfig;

/**
 * 堆叠式的 LayoutManager
 * 修改自 https://github.com/mcxtzhang/ZLayoutManager
 *
 * 页面加载后 adapter 会调用 4 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 4 个
 * 在滑动中获取到的 RecyclerView 的子 view 数有 4 个
 */
public class MySwipeCardLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 在这里处理子 view 的布局，顶部 view 拖动时底部 view 顶上去的效果不在这里处理
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        if (itemCount < 1) {
            return;
        }
        // 开始绘制的 view 的位置
        int bottomPosition;
        if (itemCount < CardConfig.MAX_SHOW_COUNT) {
            // 如果 view 总数小于最大显示数，绘制区域：[0, itemCount)
            bottomPosition = 0;
        } else {
            // 如果 view 总数大于等于最大显示数，绘制区域：[itemCount - 最大显示数, itemCount)
            bottomPosition = itemCount - CardConfig.MAX_SHOW_COUNT;
        }

        // 从可见的最底层 View 开始 layout，依次层叠上去
        for (int position = bottomPosition; position < itemCount; position++) {
            View view = recycler.getViewForPosition(position);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
            // 我们在布局时，将 childView 居中处理，这里也可以改为只水平居中
            layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view));

            /**
             * TopView的Scale 为1，translationY 0
             * 每一级Scale相差0.05f，translationY相差7dp左右
             *
             * 观察人人影视的UI，拖动时，topView被拖动，Scale不变，一直为1.
             * top-1View 的Scale慢慢变化至1，translation也慢慢恢复0
             * top-2View的Scale慢慢变化至 top-1View的Scale，translation 也慢慢变化只top-1View的translation
             * top-3View的Scale要变化，translation岿然不动
             */

            // 第几层,举例子，count = 7， 最后一个 TopView（6）是第 0 层
            int level = itemCount - position - 1;
            // 顶层不需要缩小和位移
            if (level == 0)
                continue;
            // 每一层都需要 X 方向的缩小
            view.setScaleX(1 - CardConfig.SCALE_GAP * level);
            if (level < CardConfig.MAX_SHOW_COUNT - 1) {
                // 前 N 层，依次向下位移，并在 Y 方向进行缩小
                view.setTranslationY(CardConfig.TRANS_Y_GAP * level);
                view.setScaleY(1 - CardConfig.SCALE_GAP * level);
            } else {
                //第 N 层的向下位移量和 Y 方向的缩小量与 N-1 层保持一致
                view.setTranslationY(CardConfig.TRANS_Y_GAP * (level - 1));
                view.setScaleY(1 - CardConfig.SCALE_GAP * (level - 1));
            }
        }
    }
}
