package com.cashow.hencoderdemo.chapter_1_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

public class DrawPointView extends BaseView {
    private Paint paint;

    public DrawPointView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public DrawPointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();

        switch (viewType) {
            case 0:
                paint.setStrokeWidth(20);
                paint.setStrokeCap(Paint.Cap.ROUND);
                break;
            case 1:
                paint.setStrokeWidth(20);
                paint.setStrokeCap(Paint.Cap.SQUARE);
                break;
            case 2:
                paint.setStrokeWidth(20);
                paint.setStrokeCap(Paint.Cap.SQUARE);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * x 和 y 是点的坐标。
         * 点的大小可以通过 paint.setStrokeWidth(width) 来设置；
         * 点的形状可以通过  paint.setStrokeCap(cap) 来设置：ROUND 画出来是圆形的点，SQUARE 或 BUTT 画出来是方形的点。
         * drawPoints 可以画多个点。pts 这个数组是点的坐标，每两个成一对；offset 表示跳过数组的前几个数再开始记坐标；count 表示一共要绘制几个点。
         */
        switch (viewType) {
            case 0:
            case 1:
                canvas.drawPoint(50, 50, paint);
                break;
            case 2:
                float[] points = {0, 0, 50, 50, 50, 100, 100, 50, 100, 100, 150, 50, 150, 100};
                // 绘制四个点：(50, 50) (50, 100) (100, 50) (100, 100)
                canvas.drawPoints(points, 2 /* 跳过两个数，即前两个 0 */, 8 /* 一共绘制 8 个数（4 个点）*/, paint);
                break;
        }
    }
}
