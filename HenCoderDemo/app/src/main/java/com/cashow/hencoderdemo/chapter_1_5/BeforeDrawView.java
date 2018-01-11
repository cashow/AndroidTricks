package com.cashow.hencoderdemo.chapter_1_5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class BeforeDrawView extends EditText {
    private Paint paint;

    public BeforeDrawView(Context context) {
        super(context);
        init();
    }

    public BeforeDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BeforeDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        setText("asdhkjwdhoqwhoiw");
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#66BB6A"));
        super.draw(canvas);
    }
}
