package com.cashow.hencoderdemo.chapter_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

public class PaintPathEffectView extends BaseView {
    private Paint paint;
    private Path path;

    public PaintPathEffectView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public PaintPathEffectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintPathEffectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        path = new Path();
        /**
         * setPathEffect(PathEffect effect)
         * 使用 PathEffect 来给图形的轮廓设置效果。对 Canvas 所有的图形绘制有效，也就是 drawLine() drawCircle() drawPath() 这些方法。
         *
         * 把所有拐角变成圆角
         * CornerPathEffect(float radius)
         * radius 是圆角的半径
         *
         * 把线条进行随机的偏离
         * DiscretePathEffect
         * DiscretePathEffect 具体的做法是，把绘制改为使用定长的线段来拼接，并且在拼接的时候对路径进行随机偏离。
         * 它的构造方法 DiscretePathEffect(float segmentLength, float deviation) 的两个参数中，
         * segmentLength 是用来拼接的每个线段的长度，
         * deviation 是偏离量。
         */
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        path.rLineTo(50, 100);
        path.rLineTo(50, -100);
        path.rLineTo(70, 200);
        path.rLineTo(40, -150);
        switch (viewType) {
            case 0:
                break;
            case 1:
                PathEffect cornerPathEffect = new CornerPathEffect(20);
                paint.setPathEffect(cornerPathEffect);
                break;
            case 2:
                PathEffect discretePathEffect1 = new DiscretePathEffect(20, 5);
                paint.setPathEffect(discretePathEffect1);
                break;
            case 3:
                PathEffect discretePathEffect2 = new DiscretePathEffect(5, 5);
                paint.setPathEffect(discretePathEffect2);
                break;
            case 4:
                PathEffect discretePathEffect3 = new DiscretePathEffect(5, 15);
                paint.setPathEffect(discretePathEffect3);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }
}
