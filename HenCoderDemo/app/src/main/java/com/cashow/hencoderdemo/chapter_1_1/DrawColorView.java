package com.cashow.hencoderdemo.chapter_1_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

import org.adrianwalker.multilinestring.Multiline;

public class DrawColorView extends BaseView {
    private Paint paint;

    public DrawColorView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public DrawColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(logoBitmap, 0, 0, paint);
        switch (viewType) {
            case 1:
                canvas.drawColor(Color.BLACK);
                break;
            case 2:
                canvas.drawColor(Color.parseColor("#88880000"));
                break;
            case 3:
                canvas.drawRGB(100, 200, 100);
                break;
            case 4:
                canvas.drawARGB(100, 100, 200, 100);
                break;
        }
    }


    /**
     * 原图
     */
    @Multiline
    static String INFO_0;

    /**
     * drawColor(Color.BLACK);
     */
    @Multiline
    static String INFO_1;

    /**
     * drawColor(Color.parseColor("#88880000"));
     */
    @Multiline
    static String INFO_2;

    /**
     * drawRGB(100, 200, 100);
     */
    @Multiline
    static String INFO_3;

    /**
     * drawARGB(100, 100, 200, 100);
     */
    @Multiline
    static String INFO_4;
}
