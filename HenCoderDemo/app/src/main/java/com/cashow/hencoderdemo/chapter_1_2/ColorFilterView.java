package com.cashow.hencoderdemo.chapter_1_2;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

import org.adrianwalker.multilinestring.Multiline;

public class ColorFilterView extends BaseView {
    private Paint paint;
    private Shader shader;

    public ColorFilterView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public ColorFilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        /**
         * setColorFilter(ColorFilter colorFilter)
         * 为绘制设置颜色过滤。颜色过滤的意思，就是为绘制的内容设置一个统一的过滤策略，然后 Canvas.drawXXX() 方法会对每个像素都进行过滤后再绘制出来。
         * 在 Paint 里设置 ColorFilter ，使用的是 Paint.setColorFilter(ColorFilter filter) 方法。
         * ColorFilter 并不直接使用，而是使用它的子类。
         * 它共有三个子类：LightingColorFilter PorterDuffColorFilter 和 ColorMatrixColorFilter。
         *
         * LightingColorFilter 是用来模拟简单的光照效果的
         * LightingColorFilter(int mul, int add)
         * mul 和 add 都是和颜色值格式相同的 int 值，其中 mul 用来和目标像素相乘，add 用来和目标像素相加
         * R' = R * mul.R / 0xff + add.R
         * G' = G * mul.G / 0xff + add.G
         * B' = B * mul.B / 0xff + add.B
         *
         * PorterDuffColorFilter
         * 这个的作用是使用一个指定的颜色和一种指定的 PorterDuff.Mode 来与绘制对象进行合成。
         * PorterDuffColorFilter(int color, PorterDuff.Mode mode)
         * color 参数是指定的颜色， mode 参数是指定的 PorterDuff.Mode ，不过和  ComposeShader 不同的是，PorterDuffColorFilter 作为一个 ColorFilter，只能指定一种颜色作为源，而不是一个 Bitmap。
         *
         * ColorMatrixColorFilter
         * 使用一个 ColorMatrix 来对颜色进行处理
         * ColorMatrix 这个类，内部是一个 4x5 的矩阵：
         * [ a, b, c, d, e,
         * f, g, h, i, j,
         * k, l, m, n, o,
         * p, q, r, s, t ]
         * 通过计算， ColorMatrix 可以把要绘制的像素进行转换。对于颜色 [R, G, B, A] ，转换算法是这样的：
         * R’ = a*R + b*G + c*B + d*A + e;
         * G’ = f*R + g*G + h*B + i*A + j;
         * B’ = k*R + l*G + m*B + n*A + o;
         * A’ = p*R + q*G + r*B + s*A + t;
         */
        shader = new BitmapShader(logoBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        switch (viewType) {
            case 1:
                ColorFilter lightingColorFilter = new LightingColorFilter(0xff33ff, 0x000000);
                paint.setColorFilter(lightingColorFilter);
                break;
            case 2:
                ColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.parseColor("#aaffffff"), PorterDuff.Mode.SRC_OVER);
                paint.setColorFilter(porterDuffColorFilter);
                break;
            case 3:
                // 将每个像素的 (R, G, B, A) 转成 (G, B, R, 0.5 * A)
                float[] colorMatrix = new float[]{
                        0.0f, 1.0f, 0.0f, 0.0f, 0.0f, // R’ = a*R + b*G + c*B + d*A + e;
                        0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // G’ = f*R + g*G + h*B + i*A + j;
                        1.0f, 0.0f, 0.0f, 0.0f, 0.0f, // B’ = k*R + l*G + m*B + n*A + o;
                        0.0f, 0.0f, 0.0f, 0.5f, 0.0f // A’ = p*R + q*G + r*B + s*A + t;
                };
                ColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
                paint.setColorFilter(colorMatrixColorFilter);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setShader(shader);
        canvas.drawRect(0, 0, 300, 300, paint);
    }

    /**
     * setColorFilter(ColorFilter colorFilter)
     * 为绘制设置颜色过滤。颜色过滤的意思，就是为绘制的内容设置一个统一的过滤策略，然后 Canvas.drawXXX() 方法会对每个像素都进行过滤后再绘制出来。
     * 在 Paint 里设置 ColorFilter ，使用的是 Paint.setColorFilter(ColorFilter filter) 方法。
     * ColorFilter 并不直接使用，而是使用它的子类。
     * 它共有三个子类：LightingColorFilter PorterDuffColorFilter 和 ColorMatrixColorFilter。
     *
     * 原图
     */
    @Multiline
    static String INFO_0;

    /**
     * LightingColorFilter 是用来模拟简单的光照效果的
     * LightingColorFilter(int mul, int add)
     * mul 和 add 都是和颜色值格式相同的 int 值，其中 mul 用来和目标像素相乘，add 用来和目标像素相加
     * R' = R * mul.R / 0xff + add.R
     * G' = G * mul.G / 0xff + add.G
     * B' = B * mul.B / 0xff + add.B
     *
     * ColorFilter lightingColorFilter = new LightingColorFilter(0xff33ff, 0x000000)
     * paint.setColorFilter(lightingColorFilter)
     */
    @Multiline
    static String INFO_1;

    /**
     * PorterDuffColorFilter
     * 这个的作用是使用一个指定的颜色和一种指定的 PorterDuff.Mode 来与绘制对象进行合成。
     * PorterDuffColorFilter(int color, PorterDuff.Mode mode)
     * color 参数是指定的颜色， mode 参数是指定的 PorterDuff.Mode ，不过和 ComposeShader 不同的是，
     * PorterDuffColorFilter 作为一个 ColorFilter，只能指定一种颜色作为源，而不是一个 Bitmap。
     *
     * ColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.parseColor("#aaffffff"), PorterDuff.Mode.SRC_OVER)
     * paint.setColorFilter(porterDuffColorFilter)
     */
    @Multiline
    static String INFO_2;

    /**
     * ColorMatrixColorFilter
     * 使用一个 ColorMatrix 来对颜色进行处理
     * ColorMatrix 这个类，内部是一个 4x5 的矩阵：
     * [ a, b, c, d, e,
     * f, g, h, i, j,
     * k, l, m, n, o,
     * p, q, r, s, t ]
     * 通过计算， ColorMatrix 可以把要绘制的像素进行转换。对于颜色 [R, G, B, A] ，转换算法是这样的：
     * R’ = a*R + b*G + c*B + d*A + e;
     * G’ = f*R + g*G + h*B + i*A + j;
     * B’ = k*R + l*G + m*B + n*A + o;
     * A’ = p*R + q*G + r*B + s*A + t;
     *
     * // 将每个像素的 (R, G, B, A) 转成 (G, B, R, 0.5 * A)
     * float[] colorMatrix = new float[]{
     *        0.0f, 1.0f, 0.0f, 0.0f, 0.0f, // R’ = a*R + b*G + c*B + d*A + e;
     *        0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // G’ = f*R + g*G + h*B + i*A + j;
     *        1.0f, 0.0f, 0.0f, 0.0f, 0.0f, // B’ = k*R + l*G + m*B + n*A + o;
     *        0.0f, 0.0f, 0.0f, 0.5f, 0.0f // A’ = p*R + q*G + r*B + s*A + t;
     * }
     * ColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix)
     * paint.setColorFilter(colorMatrixColorFilter)
     */
    @Multiline
    static String INFO_3;
}
