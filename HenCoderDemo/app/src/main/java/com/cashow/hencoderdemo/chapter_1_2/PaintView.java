package com.cashow.hencoderdemo.chapter_1_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

import org.adrianwalker.multilinestring.Multiline;

public class PaintView extends BaseView {
    private Paint paint;
    private Path path;

    public PaintView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        path = new Path();
        /**
         * 设置抗锯齿
         * setAntiAlias (boolean aa)
         * 除了 setAntiAlias(aa) 方法，打开抗锯齿还有一个更方便的方式：构造方法。
         * 创建 Paint 对象的时候，构造方法的参数里加一个  ANTI_ALIAS_FLAG 的 flag，就可以在初始化的时候就开启抗锯齿。
         *
         * 设置图形是线条风格还是填充风格
         * setStyle(Paint.Style style)
         * Paint.Style.FILL : 填充
         * Paint.Style.STROKE : 画线
         * Paint.Style.FILL_AND_STROKE : 填充 + 画线
         *
         * 设置线条宽度。单位为像素，默认值是 0。
         * setStrokeWidth(float width)
         * 线条宽度为 0 时，它依然能够画出线，线条的宽度为 1 像素
         * 线条宽度 0 和 1 的区别在于，
         * 你可以为 Canvas 设置 Matrix 来实现几何变换（如放大、缩小、平移、旋转），
         * 在几何变换之后 Canvas 绘制的内容就会发生相应变化，包括线条也会加粗，
         * 例如 2 像素宽度的线条在 Canvas 放大 2 倍后会被以 4 像素宽度来绘制。
         * 而当线条宽度被设置为 0 时，它的宽度就被固定为 1 像素，就算 Canvas 通过几何变换被放大，它也依然会被以 1 像素宽度来绘制。
         * Google 在文档中把线条宽度为 0 时称作「hairline mode（发际线模式）」。
         *
         * 设置线头的形状
         * setStrokeCap(Paint.Cap cap)
         * 线头形状有三种：BUTT 平头、ROUND 圆头、SQUARE 方头。默认为 BUTT。
         *
         * 设置拐角的形状。
         * setStrokeJoin(Paint.Join join)
         * 有三个值可以选择：MITER 尖角、 BEVEL 平角和 ROUND 圆角。默认为 MITER。
         *
         * 设置 MITER 型拐角的延长线的最大值
         * setStrokeMiter(float miter)
         * 这个方法是对于 setStrokeJoin() 的一个补充：如果拐角的角度太小，就有可能出现连接点过长的情况
         * 为了避免意料之外的过长的尖角出现， MITER 型连接点有一个额外的规则：当尖角过长时，自动改用 BEVEL 的方式来渲染连接点
         * 至于多尖的角属于过于尖，尖到需要转为使用 BEVEL 来绘制，则是由一个属性控制的，而这个属性就是 setStrokeMiter(miter) 方法中的 miter 参数。
         * miter 参数是对于转角长度的限制，具体来讲，是指尖角的外缘端点和内部拐角的距离与线条宽度的比。
         * 这个 miter limit 的默认值是 4，对应的是一个大约 29° 的锐角。
         * 默认情况下，大于这个角的尖角会被保留，而小于这个夹角的就会被「削成平头」。
         */
        switch (viewType) {
            case 0:
                paint.setAntiAlias(false);
                break;
            case 1:
                paint.setAntiAlias(true);
                break;
            case 2:
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                break;
            case 3:
                paint.setAntiAlias(true);
                paint.setStrokeWidth(60);
                paint.setStyle(Paint.Style.STROKE);
                break;
            case 4:
                paint.setAntiAlias(true);
                paint.setStrokeWidth(60);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                break;
            case 5:
                paint.setStrokeWidth(60);
                paint.setStrokeCap(Paint.Cap.BUTT);
                break;
            case 6:
                paint.setStrokeWidth(60);
                paint.setStrokeCap(Paint.Cap.ROUND);
                break;
            case 7:
                paint.setStrokeWidth(60);
                paint.setStrokeCap(Paint.Cap.SQUARE);
                break;
            case 8:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(60);
                paint.setStrokeJoin(Paint.Join.MITER);
                path.moveTo(0, 60);
                path.rLineTo(200, 0);
                path.rLineTo(-100, 200);
                break;
            case 9:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(60);
                paint.setStrokeJoin(Paint.Join.BEVEL);
                path.moveTo(0, 60);
                path.rLineTo(200, 0);
                path.rLineTo(-100, 200);
                break;
            case 10:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(60);
                paint.setStrokeJoin(Paint.Join.ROUND);
                path.moveTo(0, 60);
                path.rLineTo(200, 0);
                path.rLineTo(-100, 200);
                break;
            case 11:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(60);
                paint.setStrokeJoin(Paint.Join.MITER);
                path.moveTo(0, 60);
                path.rLineTo(200, 0);
                path.rLineTo(-150, 80);
                break;
            case 12:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(60);
                paint.setStrokeJoin(Paint.Join.MITER);
                paint.setStrokeMiter(6);
                path.moveTo(0, 60);
                path.rLineTo(200, 0);
                path.rLineTo(-150, 80);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 13;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (viewType) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                canvas.drawCircle(150, 150, 100, paint);
                break;
            case 5:
            case 6:
            case 7:
                canvas.drawLine(0, 100, 200, 100, paint);
                break;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                canvas.drawPath(path, paint);
                break;
        }
    }

    /**
     * 设置抗锯齿
     * setAntiAlias (boolean aa)
     * 除了 setAntiAlias(aa) 方法，打开抗锯齿还有一个更方便的方式：构造方法。
     * 创建 Paint 对象的时候，构造方法的参数里加一个  ANTI_ALIAS_FLAG 的 flag，就可以在初始化的时候就开启抗锯齿。
     *
     * paint.setAntiAlias(false)
     * canvas.drawCircle(150, 150, 100, paint)
     */
    @Multiline
    static String INFO_0;

    /**
     * paint.setAntiAlias(true)
     * canvas.drawCircle(150, 150, 100, paint)
     */
    @Multiline
    static String INFO_1;

    /**
     * 设置图形是线条风格还是填充风格
     * setStyle(Paint.Style style)
     * Paint.Style.FILL : 填充
     * Paint.Style.STROKE : 画线
     * Paint.Style.FILL_AND_STROKE : 填充 + 画线
     *
     * 设置线条宽度。单位为像素，默认值是 0。
     * setStrokeWidth(float width)
     * 线条宽度为 0 时，它依然能够画出线，线条的宽度为 1 像素
     * 线条宽度 0 和 1 的区别在于，
     * 你可以为 Canvas 设置 Matrix 来实现几何变换（如放大、缩小、平移、旋转），
     * 在几何变换之后 Canvas 绘制的内容就会发生相应变化，包括线条也会加粗，
     * 例如 2 像素宽度的线条在 Canvas 放大 2 倍后会被以 4 像素宽度来绘制。
     * 而当线条宽度被设置为 0 时，它的宽度就被固定为 1 像素，就算 Canvas 通过几何变换被放大，它也依然会被以 1 像素宽度来绘制。
     * Google 在文档中把线条宽度为 0 时称作「hairline mode（发际线模式）」。
     *
     * paint.setAntiAlias(true);
     * paint.setStyle(Paint.Style.FILL);
     * canvas.drawCircle(150, 150, 100, paint)
     */
    @Multiline
    static String INFO_2;

    /**
     * paint.setAntiAlias(true);
     * paint.setStrokeWidth(60);
     * paint.setStyle(Paint.Style.STROKE);
     * canvas.drawCircle(150, 150, 100, paint)
     */
    @Multiline
    static String INFO_3;

    /**
     * paint.setAntiAlias(true);
     * paint.setStrokeWidth(60);
     * paint.setStyle(Paint.Style.FILL_AND_STROKE);
     * canvas.drawCircle(150, 150, 100, paint)
     */
    @Multiline
    static String INFO_4;

    /**
     * 设置线头的形状
     * setStrokeCap(Paint.Cap cap)
     * 线头形状有三种：BUTT 平头、ROUND 圆头、SQUARE 方头。默认为 BUTT。
     *
     * paint.setStrokeWidth(60);
     * paint.setStrokeCap(Paint.Cap.BUTT);
     * canvas.drawLine(0, 100, 200, 100, paint)
     */
    @Multiline
    static String INFO_5;

    /**
     * paint.setStrokeWidth(60);
     * paint.setStrokeCap(Paint.Cap.ROUND);
     * canvas.drawLine(0, 100, 200, 100, paint)
     */
    @Multiline
    static String INFO_6;

    /**
     * paint.setStrokeWidth(60);
     * paint.setStrokeCap(Paint.Cap.SQUARE);
     * canvas.drawLine(0, 100, 200, 100, paint)
     */
    @Multiline
    static String INFO_7;

    /**
     * 设置拐角的形状。
     * setStrokeJoin(Paint.Join join)
     * 有三个值可以选择：MITER 尖角、 BEVEL 平角和 ROUND 圆角。默认为 MITER。
     *
     * paint.setStyle(Paint.Style.STROKE);
     * paint.setStrokeWidth(60);
     * paint.setStrokeJoin(Paint.Join.MITER);
     * path.moveTo(0, 60);
     * path.rLineTo(200, 0);
     * path.rLineTo(-100, 200);
     * canvas.drawPath(path, paint)
     */
    @Multiline
    static String INFO_8;

    /**
     * paint.setStyle(Paint.Style.STROKE);
     * paint.setStrokeWidth(60);
     * paint.setStrokeJoin(Paint.Join.BEVEL);
     * path.moveTo(0, 60);
     * path.rLineTo(200, 0);
     * path.rLineTo(-100, 200);
     * canvas.drawPath(path, paint)
     */
    @Multiline
    static String INFO_9;

    /**
     * paint.setStyle(Paint.Style.STROKE);
     * paint.setStrokeWidth(60);
     * paint.setStrokeJoin(Paint.Join.ROUND);
     * path.moveTo(0, 60);
     * path.rLineTo(200, 0);
     * path.rLineTo(-100, 200);
     * canvas.drawPath(path, paint)
     */
    @Multiline
    static String INFO_10;

    /**
     * 设置 MITER 型拐角的延长线的最大值
     * setStrokeMiter(float miter)
     * 这个方法是对于 setStrokeJoin() 的一个补充：如果拐角的角度太小，就有可能出现连接点过长的情况
     * 为了避免意料之外的过长的尖角出现， MITER 型连接点有一个额外的规则：当尖角过长时，自动改用 BEVEL 的方式来渲染连接点
     * 至于多尖的角属于过于尖，尖到需要转为使用 BEVEL 来绘制，则是由一个属性控制的，而这个属性就是 setStrokeMiter(miter) 方法中的 miter 参数。
     * miter 参数是对于转角长度的限制，具体来讲，是指尖角的外缘端点和内部拐角的距离与线条宽度的比。
     * 这个 miter limit 的默认值是 4，对应的是一个大约 29° 的锐角。
     * 默认情况下，大于这个角的尖角会被保留，而小于这个夹角的就会被「削成平头」。
     *
     * 默认：
     * paint.setStyle(Paint.Style.STROKE);
     * paint.setStrokeWidth(60);
     * paint.setStrokeJoin(Paint.Join.MITER);
     * path.moveTo(0, 60);
     * path.rLineTo(200, 0);
     * path.rLineTo(-150, 80);
     * canvas.drawPath(path, paint)
     */
    @Multiline
    static String INFO_11;

    /**
     * 设置了 setStrokeMiter：
     * paint.setStyle(Paint.Style.STROKE);
     * paint.setStrokeWidth(60);
     * paint.setStrokeJoin(Paint.Join.MITER);
     * paint.setStrokeMiter(6);
     * path.moveTo(0, 60);
     * path.rLineTo(200, 0);
     * path.rLineTo(-150, 80);
     * canvas.drawPath(path, paint)
     */
    @Multiline
    static String INFO_12;
}
