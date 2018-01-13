package com.cashow.hencoderdemo.chapter_1_8;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cashow.hencoderdemo.R;

public class HardwareLayerPaintView extends ImageView {
    private ColorMatrix colorMatrix;
    private Paint paint;

    public HardwareLayerPaintView(Context context) {
        super(context);
        init();
    }

    public HardwareLayerPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HardwareLayerPaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setImageResource(R.drawable.ic_launcher);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(300, 300);
        setLayoutParams(params);

        colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        setLayerType(LAYER_TYPE_HARDWARE, paint);
    }
}
