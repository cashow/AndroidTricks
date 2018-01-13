package com.cashow.hencoderdemo.chapter_1_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

public class DrawOvalView extends BaseView {
    private Paint paint;

    public DrawOvalView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public DrawOvalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawOvalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();

        switch (viewType) {
            case 0:
                paint.setStyle(Paint.Style.FILL);
                break;
            case 1:
                paint.setStyle(Paint.Style.STROKE);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 只能绘制横着的或者竖着的椭圆，不能绘制斜的（斜的倒是也可以，但不是直接使用 drawOval()，而是配合几何变换）。
         * left, top, right, bottom 是这个椭圆的左、上、右、下四个边界点的坐标。
         */
        canvas.drawOval(0, 0, 200, 100, paint);
    }

    @Override
    public String getViewTypeInfo(int viewType) {
        switch (viewType) {
            case 0:
                return "drawOval(float left, float top, float right, float bottom, Paint paint)：\n" +
                        "left, top, right, bottom 是这个椭圆的左、上、右、下四个边界点的坐标\n\n" +
                        "paint.setStyle(Paint.Style.FILL)\n" +
                        "canvas.drawOval(0, 0, 200, 100, paint)";
            case 1:
                return "paint.setStyle(Paint.Style.STROKE)\n" +
                        "canvas.drawOval(0, 0, 200, 100, paint)";
        }
        return super.getViewTypeInfo(viewType);
    }
}
