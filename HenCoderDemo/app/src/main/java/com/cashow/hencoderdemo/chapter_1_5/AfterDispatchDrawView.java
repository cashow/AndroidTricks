package com.cashow.hencoderdemo.chapter_1_5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cashow.hencoderdemo.R;

public class AfterDispatchDrawView extends LinearLayout {
    private Paint paint;
    private Context context;

    public AfterDispatchDrawView(Context context) {
        super(context);
        init(context);
    }

    public AfterDispatchDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AfterDispatchDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        addImageView();
    }

    private void addImageView() {
        ImageView myImageView = new ImageView(context);
        myImageView.setImageResource(R.drawable.ic_launcher);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200);
        myImageView.setLayoutParams(layoutParams);
        addView(myImageView);
    }

    /**
     * 只要重写 dispatchDraw()，并在 super.dispatchDraw() 的下面写上你的绘制代码，这段绘制代码就会发生在子 View 的绘制之后，从而让绘制内容盖住子 View 了。
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawRect(0, 0, 400, 60, paint);
    }
}
