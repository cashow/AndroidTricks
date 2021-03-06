package com.cashow.hencoderdemo.chapter_1_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

import org.adrianwalker.multilinestring.Multiline;

public class DrawCircleView extends BaseView {
    private Paint paint;

    public DrawCircleView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public DrawCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();

        switch (viewType) {
            case 1:
                paint.setColor(Color.RED);
                break;
            case 2:
                paint.setStyle(Paint.Style.STROKE);
                break;
            case 3:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(20);
                break;
            case 4:
                paint.setStyle(Paint.Style.STROKE);
                paint.setAntiAlias(true);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 前两个参数 centerX centerY 是圆心的坐标，
         * 第三个参数 radius 是圆的半径，单位都是像素，它们共同构成了这个圆的基本信息（即用这几个信息可以构建出一个确定的圆）；
         * 第四个参数 paint 提供基本信息之外的所有风格信息，例如颜色、线条粗细、阴影等。
         */
        canvas.drawCircle(150, 150, 100, paint);
    }

    /**
     * drawCircle(float centerX, float centerY, float radius, Paint paint)：
     * centerX centerY 是圆心的坐标，radius 是圆的半径，单位都是像素。
     *
     * canvas.drawCircle(150, 150, 100, paint)
     */
    @Multiline
    static String INFO_0;

    /**
     * paint.setColor(Color.RED);
     */
    @Multiline
    static String INFO_1;

    /**
     * paint.setStyle(Paint.Style.STROKE);
     */
    @Multiline
    static String INFO_2;

    /**
     * paint.setStyle(Paint.Style.STROKE);
     * paint.setStrokeWidth(20);
     */
    @Multiline
    static String INFO_3;

    /**
     * paint.setStyle(Paint.Style.STROKE);
     * paint.setAntiAlias(true);
     */
    @Multiline
    static String INFO_4;
}
