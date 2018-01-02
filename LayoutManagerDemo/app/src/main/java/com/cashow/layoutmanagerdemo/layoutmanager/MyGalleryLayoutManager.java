package com.cashow.layoutmanagerdemo.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cashow.layoutmanagerdemo.MLog;

/**
 * 介绍：一个酷炫画廊效果，假设所有Item大小一样
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * 主页：http://blog.csdn.net/zxt0601
 * 时间： 2016/12/23.
 */

public class MyGalleryLayoutManager extends RecyclerView.LayoutManager {

    //private int mFirstVisiblePosition, mLastVisiblePosition;

    private int mChildWidth, mChildHeight;

    /**
     * 根据dx 回收界面不可见的View
     */
    protected int mLastRecyclePosition = -1;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (getItemCount() == 0) {
            // 没有 Item，界面空着吧
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            // state.isPreLayout() 是支持动画的
            return;
        }
        //detachAndScrapAttachedViews(recycler);

        if (mChildHeight == 0 || mChildWidth == 0) {
            View firstView = recycler.getViewForPosition(0);
            addView(firstView);
            measureChildWithMargins(firstView, 0, 0);
            mChildWidth = getDecoratedMeasuredWidth(firstView);
            mChildHeight = getDecoratedMeasuredHeight(firstView);
            removeAndRecycleView(firstView, recycler);
        }

        fill(recycler, state, 0);
        MLog.d("循环f @$@$@@!!!!!!!!!!!!!!!!!() onLayoutChildren: " + mLastRecyclePosition + ",childcouht:" + getChildCount());
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int realOffset;
        //先考虑滑动位移进行View的回收、填充(fill（）函数)，然后再真正的位移这些子Item。
        realOffset = fill(recycler, state, dx);
        offsetChildrenHorizontal(-realOffset);
        return realOffset;
    }

    public int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int dx) {
        MLog.d("fill() called with: recycler = [" + recycler + "], state = [" + state + "], dx = [" + dx + "]");
        // 回收越界的子 View
        recycleHideViews(recycler, state, dx);

        // 给每个已添加的 view 做动画
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            changeViewUIProperty(dx, child);
        }

        //Step2.  layout right views
        int itemCount = getItemCount();
        if (dx >= 0) {
            return addChildViewWhenScrollRight(recycler, dx, itemCount);
        } else {
            return addChildViewWhenScrollLeft(recycler, dx);
        }
    }

    private int addChildViewWhenScrollRight(RecyclerView.Recycler recycler, int dx, int itemCount) {
        View child;
        int startPos = 0;
        if (mLastRecyclePosition != -1) {
            MLog.d("循环fill() mLastRecyclePosition: " + mLastRecyclePosition + ",childcouht:" + getChildCount());
            //界面 1 2 ， 1回收， 应该从 3 = 1 + 1 + 1.
            startPos = mLastRecyclePosition + getChildCount() + 1;
        }
        int left = getPaddingLeft();
        int top = getPaddingTop();

        //如果界面上还有子View
        if (getChildCount() > 0) {
            child = getChildAt(getChildCount() - 1);
            int lastPosition = getPosition(child);
            startPos = lastPosition + 1;
            left = getNextViewLeft(child);
        }

        for (int i = startPos; i < itemCount; i++) {
            MLog.d("循环add() called with: i = [" + i + ",childcouht:" + getChildCount());
            //如果左边界已经大于屏幕可见(考虑offset)
            if (left - dx > getWidth() - getPaddingRight()) {
                MLog.d("循环break() called with: i = [" + i + ",childcouht:" + getChildCount());
                break;
            }

            child = recycler.getViewForPosition(i);
            addView(child);

            //measure 还是需要的
            measureChildWithMargins(child, 0, 0);

            changeViewUIProperty(dx, child);

            layoutDecoratedWithMargins(child, left, top
                    , left + mChildWidth, top + mChildHeight);
            left += mChildWidth;
        }
        return dx;
    }

    private int addChildViewWhenScrollLeft(RecyclerView.Recycler recycler, int dx) {
        //Step2.  layout left views
        //这种情况屏幕上一定有子View
        View leftChild = getChildAt(0);
        int endPos = getPosition(leftChild) - 1;
        if (mLastRecyclePosition != -1) {
            //界面 1 2 ， 2回收， 应该从 0 = 2 - 1 - 1.
            endPos = mLastRecyclePosition - getChildCount() - 1;
        }

        int right = getLastViewRight(leftChild);
        int top = getPaddingTop();

        for (int pos = endPos; pos >= 0; pos--) {
            //只layout可见的
            if (right - dx < getPaddingLeft()) {
                break;
            }
            leftChild = recycler.getViewForPosition(pos);
            //这里是重点重点重点！！！作者每次在这里都踩坑，
            addView(leftChild, 0);

            measureChildWithMargins(leftChild, 0, 0);

            changeViewUIProperty(dx, leftChild);

            layoutDecoratedWithMargins(leftChild, right - mChildWidth, top
                    , right, top + mChildHeight);
            right -= mChildWidth;
        }
        return dx;
    }

    /**
     * 根据滑动值改变View的UI状态
     *
     * @param dx
     * @param child
     */
    private void changeViewUIProperty(int dx, View child) {
        int parentMiddle = getPaddingLeft() + getWidth() / 2;
        int childMiddle = (int) (child.getX() + child.getWidth() / 2);
        int distance = parentMiddle - childMiddle;
        float fraction = distance * 1.0f / getWidth() / 2;
        // Counteract the default slide transition
        //child.setTranslationX(child.getWidth() * -fraction);
        // Scale the page down (between MIN_SCALE and 1)
        scale(child, fraction);

        rotation(child, fraction);
    }

    private void rotation(View child, float fraction) {
        child.setRotationY(45 * fraction);
        child.setAlpha(0.5f * (1 - fraction) + 0.5f);
    }

    private void scale(View child, float fraction) {
        final float MIN_SCALE = 0.75f;
        float scaleFactor = MIN_SCALE
                + (1 - MIN_SCALE) * (1 - Math.abs(fraction));
        child.setScaleX(scaleFactor);
        child.setScaleY(scaleFactor);
    }

    private void recycleHideViews(RecyclerView.Recycler recycler, RecyclerView.State state, int dx) {
        int childCount = getChildCount();
        if (childCount > 0 && dx != 0) {
            mLastRecyclePosition = -1;
            for (int i = childCount - 1; i >= 0; i--) {
                View child = getChildAt(i);
                if (dx > 0) {
                    //load right,recycle left
                    //child的右边不再屏幕内 recycle
                    if (getDecoratedRight(child) - dx < getPaddingLeft()) {
                        MLog.d("循环 删除 () called with: getPosition(child) = [" + getPosition(child) + "], mLastRecyclePosition = [" + mLastRecyclePosition + "], dx = [" + dx + "]");
                        //逆序的 所以取最大的
                        mLastRecyclePosition = Math.max(mLastRecyclePosition, getPosition(child));
                        removeAndRecycleView(child, recycler);
                    } else {
                        //mFirstVisiblePosition = i;
                        continue;
                    }
                } else {
                    //load left,recycle right
                    //child 的左边 不在屏幕内 recycle
                    if (getDecoratedLeft(child) - dx > getWidth() - getPaddingRight()) {
                        MLog.d("循环 删除 () called with: getPosition(child) = [" + getPosition(child) + "], mLastRecyclePosition = [" + mLastRecyclePosition + "], dx = [" + dx + "]");
                        //本身就是应该逆序，所以直接取
                        mLastRecyclePosition = getPosition(child);
                        removeAndRecycleView(child, recycler);
                    } else {
                        //mLastVisiblePosition = i;
                        continue;
                    }
                }
            }
        }
    }

    /**
     * 获取某个childView在水平方向所占的空间
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasurementHorizontal(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin;
    }

    /**
     * 获取下一个View的left
     *
     * @param view
     * @return
     */
    public int getNextViewLeft(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedRight(view) + params.rightMargin;
    }

    /**
     * 获取上一个View的Right
     *
     * @param view
     * @return
     */
    public int getLastViewRight(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedLeft(view) - params.leftMargin;
    }

    /**
     * 获取某个childView在竖直方向所占的空间
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasurementVertical(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }

    public int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    public int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
}
