package com.cashow.hencoderdemo.chapter_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

public class PaintInitView extends BaseView {
    private Paint paint;
    private Path path;
    private String text;

    public PaintInitView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public PaintInitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintInitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        path = new Path();
        text = "lalalala";
        /**
         * reset()
         * 重置 Paint 的所有属性为默认值。相当于重新 new 一个，不过性能当然高一些啦。
         *
         * set(Paint src)
         * 把 src 的所有属性全部复制过来。相当于调用 src 所有的 get 方法，然后调用这个 Paint 的对应的  set 方法来设置它们。
         *
         * setFlags(int flags)
         * 批量设置 flags。相当于依次调用它们的 set 方法。
         * paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);  相当于
         * paint.setAntiAlias(true);
         * paint.setDither(true);
         * setFlags(flags) 对应的 get 方法是 int getFlags()。
         */
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30);
        paint.setColor(Color.RED);
        path.rLineTo(50, 100);
        path.rLineTo(50, -100);
        path.rLineTo(70, 200);
        path.rLineTo(40, -150);
        switch (viewType) {
            case 0:
                break;
            case 1:
                paint.reset();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(10);
                break;
            case 2:
                Paint newPaint = new Paint();
                newPaint.setStyle(Paint.Style.STROKE);
                newPaint.setStrokeWidth(15);
                paint.set(newPaint);
                break;
            case 3:
                paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }
}
