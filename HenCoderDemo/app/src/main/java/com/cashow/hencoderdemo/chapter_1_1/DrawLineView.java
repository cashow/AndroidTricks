package com.cashow.hencoderdemo.chapter_1_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

public class DrawLineView extends BaseView {
    private Paint paint;

    public DrawLineView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public DrawLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        paint.setStrokeWidth(10);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * startX, startY, stopX, stopY 分别是线的起点和终点坐标。
         */
        switch (viewType) {
            case 0:
                canvas.drawLine(0, 0, 200, 200, paint);
                break;
            case 1:
                float[] points = {0, 0, 200, 200, 200, 200, 0, 0, 0, 100, 200, 100, 100, 0, 100, 200, 200, 0, 0, 200};
                canvas.drawLines(points, paint);
                break;
        }
    }

    @Override
    public String getViewTypeInfo(int viewType) {
        switch (viewType) {
            case 0:
                return "drawLine(float startX, float startY, float stopX, float stopY, Paint paint)：\n" +
                        "startX, startY, stopX, stopY 分别是线的起点和终点坐标\n\n" +
                        "canvas.drawLine(0, 0, 200, 200, paint)";
            case 1:
                return "drawLines(float[] pts, int offset, int count, Paint paint)\n" +
                        "drawLines(float[] pts, Paint paint)：" +
                        "画线（批量）\n\n" +
                        "float[] points = {0, 0, 200, 200, 200, 200, 0, 0, 0, 100, 200, 100, 100, 0, 100, 200, 200, 0, 0, 200}\n" +
                        "canvas.drawLines(points, paint)";
        }
        return super.getViewTypeInfo(viewType);
    }
}
