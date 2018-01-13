package com.cashow.hencoderdemo.chapter_1_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

public class PaintFilterView extends BaseView {
    private Paint paint;

    public PaintFilterView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public PaintFilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        /**
         * Paint 的色彩优化有两个方法： setDither(boolean dither) 和 setFilterBitmap(boolean filter) 。
         * 它们的作用都是让画面颜色变得更加「顺眼」，但原理和使用场景是不同的。
         *
         * 设置图像的抖动
         * setDither(boolean dither)
         * 所谓抖动，是指把图像从较高色彩深度（即可用的颜色数）向较低色彩深度的区域绘制时，在图像中有意地插入噪点，通过有规律地扰乱图像来让图像对于肉眼更加真实的做法。
         * 只要加上 paint.setDither(true) ，之后的绘制就是加抖动的了。
         * 不过对于现在（2017年）而言， setDither(dither) 已经没有当年那么实用了，
         * 因为现在的 Android 版本的绘制，默认的色彩深度已经是 32 位的 ARGB_8888 ，效果已经足够清晰了。
         * 只有当你向自建的 Bitmap 中绘制，并且选择 16 位色的 ARGB_4444 或者 RGB_565 的时候，开启它才会有比较明显的效果。
         *
         * 设置是否使用双线性过滤来绘制 Bitmap 。
         * setFilterBitmap(boolean filter)
         * 图像在放大绘制的时候，默认使用的是最近邻插值过滤，这种算法简单，但会出现马赛克现象；而如果开启了双线性过滤，就可以让结果图像显得更加平滑。
         */
        switch (viewType) {
            case 0:
                break;
            case 1:
                paint.setFilterBitmap(true);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (viewType) {
            case 0:
            case 1:
                Matrix matrix = new Matrix();
                matrix.postTranslate(-80, -80);
                matrix.postScale(7.0f, 7.0f);
                canvas.drawBitmap(logoBitmap, matrix, paint);
                break;
        }
    }

    @Override
    public String getViewTypeInfo(int viewType) {
        switch (viewType) {
            case 0:
                return "Paint 的色彩优化有两个方法： setDither(boolean dither) 和 setFilterBitmap(boolean filter) 。\n" +
                        "它们的作用都是让画面颜色变得更加「顺眼」，但原理和使用场景是不同的。\n" +
                        "\n" +
                        "设置图像的抖动\n" +
                        "setDither(boolean dither)\n" +
                        "所谓抖动，是指把图像从较高色彩深度（即可用的颜色数）向较低色彩深度的区域绘制时，在图像中有意地插入噪点，通过有规律地扰乱图像来让图像对于肉眼更加真实的做法。\n" +
                        "只要加上 paint.setDither(true) ，之后的绘制就是加抖动的了。\n" +
                        "不过对于现在（2017年）而言， setDither(dither) 已经没有当年那么实用了，\n" +
                        "因为现在的 Android 版本的绘制，默认的色彩深度已经是 32 位的 ARGB_8888 ，效果已经足够清晰了。\n" +
                        "只有当你向自建的 Bitmap 中绘制，并且选择 16 位色的 ARGB_4444 或者 RGB_565 的时候，开启它才会有比较明显的效果。\n" +
                        "\n" +
                        "设置是否使用双线性过滤来绘制 Bitmap 。\n" +
                        "setFilterBitmap(boolean filter)\n" +
                        "图像在放大绘制的时候，默认使用的是最近邻插值过滤，这种算法简单，但会出现马赛克现象；而如果开启了双线性过滤，就可以让结果图像显得更加平滑。\n\n" +
                        "Matrix matrix = new Matrix();\n" +
                        "matrix.postTranslate(-80, -80);\n" +
                        "matrix.postScale(7.0f, 7.0f);\n" +
                        "canvas.drawBitmap(logoBitmap, matrix, paint);";
            case 1:
                return "paint.setFilterBitmap(true);\n" +
                        "Matrix matrix = new Matrix();\n" +
                        "matrix.postTranslate(-80, -80);\n" +
                        "matrix.postScale(7.0f, 7.0f);\n" +
                        "canvas.drawBitmap(logoBitmap, matrix, paint);";
        }
        return super.getViewTypeInfo(viewType);
    }
}
