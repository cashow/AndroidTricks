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

public class BeforeDispatchDrawView extends LinearLayout {
    private Paint paint;
    private Context context;

    public BeforeDispatchDrawView(Context context) {
        super(context);
        init(context);
    }

    public BeforeDispatchDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BeforeDispatchDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
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
     * 在绘制过程中，每一个 ViewGroup 会先调用自己的  onDraw() 来绘制完自己的主体之后再去绘制它的子 View。
     * 对于这个例子来说，就是 LinearLayout 会在绘制完斑点后再去绘制它的子 View。
     * 那么在子 View 绘制完成之后，先前绘制的就被子 View 盖住了。
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawRect(0, 0, 400, 60, paint);
        super.dispatchDraw(canvas);
    }
}
