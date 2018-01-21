package com.cashow.hencoderdemo.chapter_1_2;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cashow.hencoderdemo.common.BaseView;

import org.adrianwalker.multilinestring.Multiline;

public class PorterDuffModeView extends BaseView {
    private Paint paint;
    private Shader shader;

    public PorterDuffModeView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public PorterDuffModeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PorterDuffModeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        // 关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Shader shader1 = new BitmapShader(logoBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Shader shader2 = new BitmapShader(testBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        switch (viewType) {
            case 0:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC);
                break;
            case 1:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_OVER);
                break;
            case 2:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_IN);
                break;
            case 3:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_ATOP);
                break;
            case 4:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DST);
                break;
            case 5:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_OVER);
                break;
            case 6:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_IN);
                break;
            case 7:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_ATOP);
                break;
            case 8:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.CLEAR);
                break;
            case 9:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_OUT);
                break;
            case 10:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_OUT);
                break;
            case 11:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.XOR);
                break;
            case 12:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DARKEN);
                break;
            case 13:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.LIGHTEN);
                break;
            case 14:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.MULTIPLY);
                break;
            case 15:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SCREEN);
                break;
            case 16:
                shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.OVERLAY);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 17;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * PorterDuff.Mode 是用来指定两个图像共同绘制时的颜色策略的。
         * 它是一个 enum，不同的 Mode 可以指定不同的策略。
         * 「颜色策略」的意思，就是说把源图像绘制到目标图像处时应该怎样确定二者结合后的颜色，
         * 而对于 ComposeShader(shaderA, shaderB, mode) 这个具体的方法，
         * 就是指应该怎样把 shaderB 绘制在 shaderA 上来得到一个结合后的 Shader。
         *
         * 具体来说， PorterDuff.Mode 一共有 17 个，可以分为两类：
         * 1. Alpha 合成 (Alpha Compositing)
         * SRC, SRC_OVER, SRC_IN, SRC_ATOP, DST, DST_OVER, DST_IN, DST_ATOP, CLEAR, SRC_OUT, DST_OUT, XOR
         *
         * 2. 混合 (Blending)
         * DARKEN, LIGHTEN, MULTIPLY, SCREEN, OVERLAY
         */
        paint.setShader(shader);
        canvas.drawRect(0, 0, 300, 300, paint);
    }

    /**
     * PorterDuff.Mode 是用来指定两个图像共同绘制时的颜色策略的。
     * 它是一个 enum，不同的 Mode 可以指定不同的策略。
     * 「颜色策略」的意思，就是说把源图像绘制到目标图像处时应该怎样确定二者结合后的颜色，
     * 而对于 ComposeShader(shaderA, shaderB, mode) 这个具体的方法，
     * 就是指应该怎样把 shaderB 绘制在 shaderA 上来得到一个结合后的 Shader。
     *
     * 具体来说， PorterDuff.Mode 一共有 17 个，可以分为两类：
     * 1. Alpha 合成 (Alpha Compositing)
     * SRC, SRC_OVER, SRC_IN, SRC_ATOP, DST, DST_OVER, DST_IN, DST_ATOP, CLEAR, SRC_OUT, DST_OUT, XOR
     *
     * 2. 混合 (Blending)
     * DARKEN, LIGHTEN, MULTIPLY, SCREEN, OVERLAY
     *
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC)
     */
    @Multiline
    static String INFO_0;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_OVER)
     */
    @Multiline
    static String INFO_1;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_IN)
     */
    @Multiline
    static String INFO_2;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_ATOP)
     */
    @Multiline
    static String INFO_3;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DST)
     */
    @Multiline
    static String INFO_4;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_OVER)
     */
    @Multiline
    static String INFO_5;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_IN)
     */
    @Multiline
    static String INFO_6;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_ATOP)
     */
    @Multiline
    static String INFO_7;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.CLEAR)
     */
    @Multiline
    static String INFO_8;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_OUT)
     */
    @Multiline
    static String INFO_9;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_OUT)
     */
    @Multiline
    static String INFO_10;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.XOR)
     */
    @Multiline
    static String INFO_11;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DARKEN)
     */
    @Multiline
    static String INFO_12;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.LIGHTEN)
     */
    @Multiline
    static String INFO_13;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.MULTIPLY)
     */
    @Multiline
    static String INFO_14;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SCREEN)
     */
    @Multiline
    static String INFO_15;

    /**
     * shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.OVERLAY)
     */
    @Multiline
    static String INFO_16;
}
