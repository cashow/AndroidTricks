package com.cashow.hencoderdemo.chapter_1_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

public class DrawBitmapView extends BaseView {
    private Paint paint;

    public DrawBitmapView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public DrawBitmapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawBitmapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画 Bitmap
         * drawBitmap(Bitmap bitmap, float left, float top, Paint paint)
         * left 和 top 是要把 bitmap 绘制到的位置坐标
         */
        canvas.drawBitmap(logoBitmap, 0, 0, paint);
    }
}
