package com.cashow.listviewpulltozoom;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    private ListView listview;
    private ListviewAdapter adapter;

    // 图片是否在缩放状态
    private boolean isZooming;
    // 图片开始缩放时的触摸位置
    private float oriPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.listview);

        adapter = new ListviewAdapter(this);
        listview.setAdapter(adapter);

        Drawable drawable = getResources().getDrawable(R.drawable.listview_header_background);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        adapter.setAvatarBitmap(bitmap);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 如果移动过程中触摸点不在listview上，比如移动到了屏幕外侧或者系统的虚拟按键等
        // 这时需要将缩放状态重置
        if (!isInView(listview, event.getRawX(), event.getRawY())) {
            adapter.resetZoom();
            isZooming = false;
            return false;
        }

        // 如果图片不在缩放状态并且listview已经滑动到了顶部，图片进入缩放状态
        int top = listview.getChildAt(0).getTop();
        if (!isZooming && top == 0) {
            oriPosition = event.getRawY();
            isZooming = true;
        }

        // 如果不在缩放状态，不需要处理ACTION_MOVE和ACTION_UP事件
        if (!isZooming)
            return super.dispatchTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                // 计算触摸点与初始点的距离，根据这个距离计算图片缩放的大小
                float diff = event.getRawY() - oriPosition;
                if (diff > 0) {
                    // diff大于0表示正在往下拉，此时图片处于缩放状态，
                    // 需要拦截触摸事件并缩放图片
                    adapter.setZoomDiff(diff);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                isZooming = false;
                return adapter.resetZoom();
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    // 用来判断坐标(rawX, rawY)是否在view上
    private boolean isInView(View view, float rawX, float rawY) {
        int scrcoords[] = new int[2];
        view.getLocationOnScreen(scrcoords);
        float x = rawX + view.getLeft() - scrcoords[0];
        float y = rawY + view.getTop() - scrcoords[1];
        if (x >= view.getLeft() && x <= view.getRight() && y >= view.getTop() && y <= view.getBottom())
            return true;
        return false;
    }
}
