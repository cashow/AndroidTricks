package com.cashow.hencoderdemo.chapter_1_5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

public class BeforeOnDrawView extends TextView {
    private Paint paint;

    public BeforeOnDrawView(Context context) {
        super(context);
        init();
    }

    public BeforeOnDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BeforeOnDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        setText("asdjlkasjdidiqowdhq");
    }

    /**
     * 如果把绘制代码写在 super.onDraw() 的上面，由于绘制代码会执行在原有内容的绘制之前，所以绘制的内容会被控件的原内容盖住。
     * 相对来说，这种用法的场景就会少一些。不过只是少一些而不是没有，比如你可以通过在文字的下层绘制纯色矩形来作为「强调色」
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, 400, 60, paint);
        super.onDraw(canvas);
    }
}
