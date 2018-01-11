package com.cashow.hencoderdemo.chapter_1_5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cashow.hencoderdemo.R;

public class AfterOnDrawView extends ImageView {
    private Paint paint;

    public AfterOnDrawView(Context context) {
        super(context);
        init();
    }

    public AfterOnDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AfterOnDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(40);
        setImageResource(R.drawable.ic_launcher);
    }

    /**
     * 把绘制代码写在 super.onDraw() 的下面，由于绘制代码会在原有内容绘制结束之后才执行，所以绘制内容就会盖住控件原来的内容。
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("MyImageView", 20, 50, paint);
    }
}
