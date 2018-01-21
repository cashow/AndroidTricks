package com.cashow.hencoderdemo.chapter_1_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

import org.adrianwalker.multilinestring.Multiline;

public class DrawRectView extends BaseView {
    private Paint paint;

    public DrawRectView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public DrawRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
         * left, top, right, bottom 是矩形四条边的坐标。
         */
        canvas.drawRect(50, 50, 200, 200, paint);
    }

    /**
     * drawRect(float left, float top, float right, float bottom, Paint paint)：
     * left, top, right, bottom 是矩形四条边的坐标。
     *
     * paint.setStyle(Paint.Style.FILL);
     * canvas.drawRect(50, 50, 200, 200, paint);
     */
    @Multiline
    static String INFO_0;

    /**
     * paint.setStyle(Paint.Style.STROKE);
     * canvas.drawRect(50, 50, 200, 200, paint);
     */
    @Multiline
    static String INFO_1;
}
