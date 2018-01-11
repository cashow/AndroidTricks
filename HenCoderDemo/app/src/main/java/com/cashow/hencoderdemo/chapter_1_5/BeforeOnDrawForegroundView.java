package com.cashow.hencoderdemo.chapter_1_5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cashow.hencoderdemo.R;

public class BeforeOnDrawForegroundView extends ImageView {
    private Paint paint;
    private Context context;

    public BeforeOnDrawForegroundView(Context context) {
        super(context);
        init(context);
    }

    public BeforeOnDrawForegroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BeforeOnDrawForegroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        setImageResource(R.drawable.ic_launcher);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setForeground(context.getResources().getDrawable(R.drawable.grey_mask));
        }
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        canvas.drawRect(0, 0, 400, 60, paint);
        super.onDrawForeground(canvas);
    }
}
