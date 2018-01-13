package com.cashow.hencoderdemo.chapter_1_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

public class DrawRoundRectView extends BaseView {
    private Paint paint;

    public DrawRoundRectView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public DrawRoundRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawRoundRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
         * left, top, right, bottom 是四条边的坐标，rx 和 ry 是圆角的横向半径和纵向半径。
         */
        canvas.drawRoundRect(50, 50, 300, 200, 50, 50, paint);
    }

    @Override
    public String getViewTypeInfo(int viewType) {
        switch (viewType) {
            case 0:
                return "drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, Paint paint)：\n" +
                        "left, top, right, bottom 是四条边的坐标，rx 和 ry 是圆角的横向半径和纵向半径。\n\n" +
                        "paint.setStyle(Paint.Style.FILL)\n" +
                        "canvas.drawRoundRect(50, 50, 300, 200, 50, 50, paint)";
            case 1:
                return "paint.setStyle(Paint.Style.STROKE)\n" +
                        "canvas.drawRoundRect(50, 50, 300, 200, 50, 50, paint)";
        }
        return super.getViewTypeInfo(viewType);
    }
}
