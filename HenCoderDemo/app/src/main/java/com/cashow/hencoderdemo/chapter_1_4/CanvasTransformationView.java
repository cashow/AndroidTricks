package com.cashow.hencoderdemo.chapter_1_4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;
import com.cashow.hencoderdemo.common.BitmapUtils;

public class CanvasTransformationView extends BaseView {
    private Paint paint;

    public CanvasTransformationView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public CanvasTransformationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CanvasTransformationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        switch (viewType) {
            case 5:
            case 6:
            case 7:
            case 8:
                logoBitmap = BitmapUtils.getResizedBitmap(logoBitmap, 100, 100);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 9;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 几何变换的使用大概分为三类：
         *   使用 Canvas 来做常见的二维变换；
         *   使用 Matrix 来做常见和不常见的二维变换；
         *   使用 Camera 来做三维变换。
         *
         *
         * 使用 Canvas 来做常见的二维变换：
         * 平移
         * Canvas.translate(float dx, float dy)
         * 参数里的 dx 和 dy 表示横向和纵向的位移。
         *
         * 旋转
         * Canvas.rotate(float degrees, float px, float py)
         * 参数里的 degrees 是旋转角度，单位是度（也就是一周有 360° 的那个单位），方向是顺时针为正向；  px 和 py 是轴心的位置。
         *
         * 放缩
         * Canvas.scale(float sx, float sy, float px, float py)
         * 参数里的 sx sy 是横向和纵向的放缩倍数； px py 是放缩的轴心。
         *
         * 错切
         * skew(float sx, float sy)
         * 参数里的 sx 和 sy 是 x 方向和 y 方向的错切系数。
         */
        canvas.save();
        switch (viewType) {
            case 0:
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                break;
            case 1:
                canvas.translate(50, 50);
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                break;
            case 2:
                canvas.rotate(90, logoBitmap.getWidth() / 2, logoBitmap.getHeight() / 2);
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                break;
            case 3:
                canvas.scale(0.5f, 0.5f, logoBitmap.getWidth() / 2, logoBitmap.getHeight() / 2);
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                break;
            case 4:
                canvas.scale(0.5f, 0.5f, 50, 50);
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                break;
            case 5:
                canvas.skew(-0.5f, 0f);
                canvas.drawBitmap(logoBitmap, 100, 100, paint);
                break;
            case 6:
                canvas.skew(0.5f, 0f);
                canvas.drawBitmap(logoBitmap, 100, 100, paint);
                break;
            case 7:
                canvas.skew(0f, 0.5f);
                canvas.drawBitmap(logoBitmap, 100, 100, paint);
                break;
            case 8:
                canvas.skew(0f, -0.5f);
                canvas.drawBitmap(logoBitmap, 100, 100, paint);
                break;
        }
        canvas.restore();
    }
}
