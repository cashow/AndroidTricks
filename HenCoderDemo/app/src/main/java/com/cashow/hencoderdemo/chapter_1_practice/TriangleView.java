package com.cashow.hencoderdemo.chapter_1_practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class TriangleView extends View {
    private Paint trianglePaint;
    private Path trianglePath;

    public TriangleView(Context context) {
        super(context);
        init(context);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        trianglePaint = new Paint();
        trianglePaint.setStyle(Paint.Style.STROKE);
        trianglePaint.setStrokeWidth(5);

        trianglePath = new Path();
        trianglePath.moveTo(0, 50);
        trianglePath.rLineTo(25, -50);
        trianglePath.rLineTo(25, 50);
        trianglePath.close();

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50, 50);
        setLayoutParams(params);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(trianglePath, trianglePaint);
    }
}
