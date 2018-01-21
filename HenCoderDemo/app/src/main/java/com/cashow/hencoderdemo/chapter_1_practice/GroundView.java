package com.cashow.hencoderdemo.chapter_1_practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class GroundView extends View {
    private Paint groundPaint;

    public GroundView(Context context) {
        super(context);
        init(context);
    }

    public GroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        groundPaint = new Paint();

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        setLayoutParams(params);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0, 0, getWidth(), 1, groundPaint);
    }
}
