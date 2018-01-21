package com.cashow.hencoderdemo.chapter_1_2;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cashow.hencoderdemo.common.BaseView;

import org.adrianwalker.multilinestring.Multiline;

public class PaintColorView extends BaseView {
    private Paint paint;
    private Shader shader;

    public PaintColorView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public PaintColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        switch (viewType) {
            case 0:
                paint.setColor(Color.parseColor("#009688"));
                break;
            case 1:
                paint.setARGB(255, 255, 152, 0);
                break;
            case 2:
                shader = new LinearGradient(0, 0, 100, 100, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"), Shader.TileMode.CLAMP);
                break;
            case 3:
                shader = new LinearGradient(0, 0, 100, 100, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"), Shader.TileMode.MIRROR);
                break;
            case 4:
                shader = new LinearGradient(0, 0, 100, 100, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"), Shader.TileMode.REPEAT);
                break;
            case 5:
                shader = new RadialGradient(150, 150, 200, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"), Shader.TileMode.CLAMP);
                break;
            case 6:
                shader = new SweepGradient(150, 150, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"));
                break;
            case 7:
                shader = new BitmapShader(logoBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                break;
            case 8:
                Shader shader1 = new BitmapShader(logoBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Shader shader2 = new RadialGradient(150, 150, 200, Color.parseColor("#ccE91E63"), Color.parseColor("#cc2196F3"), Shader.TileMode.CLAMP);
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_OVER);
                break;
            case 9:
                Shader shader3 = new BitmapShader(logoBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Shader shader4 = new BitmapShader(testBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                shader = new ComposeShader(shader3, shader4, PorterDuff.Mode.SRC_OVER);
                break;
            case 10:
                Shader shader5 = new BitmapShader(logoBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Shader shader6 = new BitmapShader(testBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                // 关闭硬件加速
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                shader = new ComposeShader(shader5, shader6, PorterDuff.Mode.SRC_OVER);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 11;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * Paint 设置颜色的方法有两种：一种是直接用 Paint.setColor/ARGB() 来设置颜色，另一种是使用  Shader 来指定着色方案。
         *
         *
         * 1. 直接设置颜色
         * setColor(int color)
         * setARGB(int a, int r, int g, int b)
         *
         *
         * 2. 设置 Shader
         * setShader(Shader shader)
         * 在 Android 的绘制里使用 Shader ，并不直接用 Shader 这个类，而是用它的几个子类。
         * 具体来讲有 LinearGradient RadialGradient SweepGradient BitmapShader ComposeShader 这么几个
         * 注意：在设置了 Shader 的情况下， Paint.setColor/ARGB() 所设置的颜色就不再起作用。
         *
         * LinearGradient 线性渐变
         * LinearGradient(float x0, float y0, float x1, float y1, int color0, int color1, Shader.TileMode tile)
         * 设置两个点和两种颜色，以这两个点作为端点，使用两种颜色的渐变来绘制颜色。
         * x0 y0 x1 y1：渐变的两个端点的位置
         * color0 color1 是端点的颜色
         * tile：端点范围之外的着色规则，类型是 TileMode。
         * TileMode 一共有 3 个值可选： CLAMP, MIRROR 和 REPEAT。
         * CLAMP 会在端点之外延续端点处的颜色；
         * MIRROR 是镜像模式；
         * REPEAT 是重复模式。
         *
         * RadialGradient 辐射渐变
         * RadialGradient(float centerX, float centerY, float radius, int centerColor, int edgeColor, TileMode tileMode)。
         * 从中心向周围辐射状的渐变
         * centerX centerY：辐射中心的坐标
         * radius：辐射半径
         * centerColor：辐射中心的颜色
         * edgeColor：辐射边缘的颜色
         * tileMode：辐射范围之外的着色模式。
         *
         * SweepGradient 扫描渐变
         * SweepGradient(float cx, float cy, int color0, int color1)
         * cx cy ：扫描的中心
         * color0：扫描的起始颜色
         * color1：扫描的终止颜色
         *
         * BitmapShader 用 Bitmap 来着色
         * 用 Bitmap 的像素来作为图形或文字的填充
         * BitmapShader(Bitmap bitmap, Shader.TileMode tileX, Shader.TileMode tileY)
         * tileX：横向的 TileMode
         * tileY：纵向的 TileMode
         *
         * ComposeShader 混合着色器
         * 两个 Shader 一起使用
         * ComposeShader(Shader shaderA, Shader shaderB, PorterDuff.Mode mode)
         * shaderA, shaderB：两个相继使用的 Shader
         * mode: 两个 Shader 的叠加模式，即 shaderA 和 shaderB 应该怎样共同绘制。它的类型是 PorterDuff.Mode 。
         * ComposeShader() 在硬件加速下是不支持两个相同类型的 Shader 的，所以需要关闭硬件加速才能看到效果。
         */
        switch (viewType) {
            case 0:
                canvas.drawRect(0, 0, 400, 400, paint);
                break;
            case 1:
                canvas.drawRect(0, 0, 400, 400, paint);
                break;
            case 2:
                paint.setShader(shader);
                canvas.drawRect(0, 0, 400, 400, paint);
                break;
            case 3:
                paint.setShader(shader);
                canvas.drawRect(0, 0, 400, 400, paint);
                break;
            case 4:
                paint.setShader(shader);
                canvas.drawRect(0, 0, 400, 400, paint);
                break;
            case 5:
                paint.setShader(shader);
                canvas.drawRect(0, 0, 400, 400, paint);
                break;
            case 6:
                paint.setShader(shader);
                canvas.drawRect(0, 0, 400, 400, paint);
                break;
            case 7:
                paint.setShader(shader);
                canvas.drawCircle(150, 150, 100, paint);
                break;
            case 8:
                paint.setShader(shader);
                canvas.drawCircle(150, 150, 100, paint);
                break;
            case 9:
            case 10:
                paint.setShader(shader);
                canvas.drawRect(0, 0, 300, 300, paint);
                break;
        }
    }

    /**
     * Paint 设置颜色的方法有两种：一种是直接用 Paint.setColor/ARGB() 来设置颜色，另一种是使用  Shader 来指定着色方案。
     * 直接设置颜色：
     * setColor(int color)
     * setARGB(int a, int r, int g, int b)
     *
     * paint.setColor(Color.parseColor("#009688"))
     * canvas.drawRect(0, 0, 400, 400, paint)
     */
    @Multiline
    static String INFO_0;

    /**
     * paint.setARGB(255, 255, 152, 0)
     * canvas.drawRect(0, 0, 400, 400, paint)
     */
    @Multiline
    static String INFO_1;

    /**
     * 设置 Shader：
     * setShader(Shader shader)
     * 在 Android 的绘制里使用 Shader ，并不直接用 Shader 这个类，而是用它的几个子类。
     * 具体来讲有 LinearGradient、RadialGradient、SweepGradient、BitmapShader、ComposeShader 这么几个。
     * 注意：在设置了 Shader 的情况下，Paint.setColor/ARGB() 所设置的颜色就不再起作用。
     *
     * LinearGradient 线性渐变
     * LinearGradient(float x0, float y0, float x1, float y1, int color0, int color1, Shader.TileMode tile)
     * 设置两个点和两种颜色，以这两个点作为端点，使用两种颜色的渐变来绘制颜色。
     * x0 y0 x1 y1：渐变的两个端点的位置
     * color0 color1：是端点的颜色
     * tile：端点范围之外的着色规则，类型是 TileMode。
     * TileMode 一共有 3 个值可选： CLAMP, MIRROR 和 REPEAT。
     * CLAMP 会在端点之外延续端点处的颜色；
     * MIRROR 是镜像模式；
     * REPEAT 是重复模式。
     *
     * shader = new LinearGradient(0, 0, 100, 100, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"), Shader.TileMode.CLAMP)
     * paint.setShader(shader)
     * canvas.drawRect(0, 0, 400, 400, paint)
     */
    @Multiline
    static String INFO_2;

    /**
     * shader = new LinearGradient(0, 0, 100, 100, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"), Shader.TileMode.MIRROR)
     * paint.setShader(shader);
     * canvas.drawRect(0, 0, 400, 400, paint)
     */
    @Multiline
    static String INFO_3;

    /**
     * shader = new LinearGradient(0, 0, 100, 100, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"), Shader.TileMode.REPEAT)
     * paint.setShader(shader);
     * canvas.drawRect(0, 0, 400, 400, paint)
     */
    @Multiline
    static String INFO_4;

    /**
     * RadialGradient 辐射渐变
     * RadialGradient(float centerX, float centerY, float radius, int centerColor, int edgeColor, TileMode tileMode)。
     * 从中心向周围辐射状的渐变
     * centerX centerY：辐射中心的坐标
     * radius：辐射半径
     * centerColor：辐射中心的颜色
     * edgeColor：辐射边缘的颜色
     * tileMode：辐射范围之外的着色模式。
     *
     * shader = new RadialGradient(150, 150, 200, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"), Shader.TileMode.CLAMP)
     *
     * paint.setShader(shader);
     * canvas.drawRect(0, 0, 400, 400, paint)
     */
    @Multiline
    static String INFO_5;

    /**
     * SweepGradient 扫描渐变
     * SweepGradient(float cx, float cy, int color0, int color1)
     * cx cy ：扫描的中心
     * color0：扫描的起始颜色
     * color1：扫描的终止颜色
     *
     * shader = new SweepGradient(150, 150, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"))
     *
     * paint.setShader(shader);
     * canvas.drawRect(0, 0, 400, 400, paint)
     */
    @Multiline
    static String INFO_6;

    /**
     * BitmapShader 用 Bitmap 来着色
     * 用 Bitmap 的像素来作为图形或文字的填充
     * BitmapShader(Bitmap bitmap, Shader.TileMode tileX, Shader.TileMode tileY)
     * tileX：横向的 TileMode
     * tileY：纵向的 TileMode
     *
     * shader = new BitmapShader(logoBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
     *
     * paint.setShader(shader);
     * canvas.drawCircle(150, 150, 100, paint)
     */
    @Multiline
    static String INFO_7;

    /**
     * ComposeShader 混合着色器
     * 两个 Shader 一起使用
     * ComposeShader(Shader shaderA, Shader shaderB, PorterDuff.Mode mode)
     * shaderA, shaderB：两个相继使用的 Shader
     * mode: 两个 Shader 的叠加模式，即 shaderA 和 shaderB 应该怎样共同绘制。它的类型是 PorterDuff.Mode 。
     *
     * Shader shader1 = new BitmapShader(logoBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
     * Shader shader2 = new RadialGradient(150, 150, 200, Color.parseColor("#ccE91E63"), Color.parseColor("#cc2196F3"), Shader.TileMode.CLAMP)
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_OVER)
     *
     * paint.setShader(shader);
     * canvas.drawCircle(150, 150, 100, paint)
     */
    @Multiline
    static String INFO_8;

    /**
     * ComposeShader() 在硬件加速下是不支持两个相同类型的 Shader 的，所以需要关闭硬件加速才能看到效果。
     *
     * Shader shader3 = new BitmapShader(logoBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
     * Shader shader4 = new BitmapShader(testBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
     * shader = new ComposeShader(shader3, shader4, PorterDuff.Mode.SRC_OVER)
     *
     * paint.setShader(shader);
     * canvas.drawRect(0, 0, 300, 300, paint)
     */
    @Multiline
    static String INFO_9;

    /**
     * Shader shader5 = new BitmapShader(logoBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
     * Shader shader6 = new BitmapShader(testBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
     * // 关闭硬件加速
     * setLayerType(View.LAYER_TYPE_SOFTWARE, null)
     * shader = new ComposeShader(shader5, shader6, PorterDuff.Mode.SRC_OVER)
     *
     * paint.setShader(shader);
     * canvas.drawRect(0, 0, 300, 300, paint)
     */
    @Multiline
    static String INFO_10;
}
