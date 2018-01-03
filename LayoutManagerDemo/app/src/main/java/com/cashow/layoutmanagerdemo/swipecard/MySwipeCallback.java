package com.cashow.layoutmanagerdemo.swipecard;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.cashow.layoutmanagerdemo.adapter.MyCardAdapter;

/**
 * 介绍：人人影视效果的Callback
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * 主页：http://blog.csdn.net/zxt0601
 * 时间： 16/12/18.
 */

public class MySwipeCallback extends ItemTouchHelper.SimpleCallback {

    protected RecyclerView recyclerView;
    protected MyCardAdapter myCardAdapter;

    public MySwipeCallback(RecyclerView rv, MyCardAdapter adapter) {
        this(0,
                ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                rv, adapter);
    }

    public MySwipeCallback(int dragDirs, int swipeDirs
            , RecyclerView rv, MyCardAdapter adapter) {
        super(dragDirs, swipeDirs);
        recyclerView = rv;
        myCardAdapter = adapter;
    }

    /**
     * 移动距离超过 getThreshold() 时，底部 view 处于完全顶上来的状态并且不再有动画
     */
    private float getThreshold() {
        return recyclerView.getWidth() * 0.5f;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        myCardAdapter.removeTop();
    }

    /**
     * 子 view 拖动时的效果在这里处理
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        // 先根据滑动的 dx 和 dy，算出动画的比例系数 fraction
        double swipValue = Math.sqrt(dX * dX + dY * dY);
        double fraction = swipValue / getThreshold();
        // 边界修正，最大为1
        if (fraction > 1) {
            fraction = 1;
        }
        // 以下代码负责的是，在顶部 view 拖动时，下面的 view 逐渐顶上去的动画
        // 如果把以下代码注释掉，顶部的 view 还是能拖动，但是在顶部 view 拖动过程中，下面的 view 不会有动画
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = recyclerView.getChildAt(i);
            // 第几层。顶部 view 是第 0 层，往下递增
            int level = childCount - i - 1;
            // 第 0 层即顶部 view 不需要在拖动时添加动画
            if (level == 0)
                continue;
            child.setScaleX((float) (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP));

            if (level < CardConfig.MAX_SHOW_COUNT - 1) {
                child.setScaleY((float) (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP));
                child.setTranslationY((float) (CardConfig.TRANS_Y_GAP * level - fraction * CardConfig.TRANS_Y_GAP));
            }
        }
    }
}
