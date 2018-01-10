package com.cashow.hencoderdemo.chapter_1_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

public class DrawTextView extends BaseView {
    private Paint paint;

    public DrawTextView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public DrawTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        switch (viewType) {
            case 0:
                paint.setTextSize(18);
                break;
            case 1:
                paint.setTextSize(36);
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
         * 绘制文字
         * drawText(String text, float x, float y, Paint paint)
         * 参数  text 是用来绘制的字符串，x 和 y 是绘制的起点坐标。
         */
        canvas.drawText("hello", 200, 100, paint);
    }
}
