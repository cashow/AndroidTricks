package com.cashow.hencoderdemo.chapter_1_3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

import org.adrianwalker.multilinestring.Multiline;

public class NewDrawTextView extends BaseView {
    private Paint paint;
    private Path path;
    private String text1;
    private StaticLayout staticLayout1;

    public NewDrawTextView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public NewDrawTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NewDrawTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        path = new Path();
        path.rLineTo(50, 100);
        path.rLineTo(50, -100);
        path.rLineTo(70, 200);
        path.rLineTo(40, -150);
        text1 = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(40);
        staticLayout1 = new StaticLayout(text1, textPaint, 600, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        switch (viewType) {
            case 0:
                paint.setTextSize(80);
                break;
            case 1:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                paint.setTextSize(80);
                break;
            case 2:
                paint.setTextSize(40);
                break;
            case 3:
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
        /**
         * 绘制文字
         * drawText(String text, float x, float y, Paint paint)
         * drawText() 是 Canvas 最基本的绘制文字的方法：给出文字的内容和位置， Canvas 按要求去绘制文字。
         * x 和 y 是文字的坐标，这个坐标并不是文字的左上角，而是一个与左下角比较接近的位置
         * drawText() 参数中的 y ，指的是文字的基线（ baseline ） 的位置
         *
         * 沿着一条 Path 来绘制文字
         * drawTextOnPath(String text, Path path, float hOffset, float vOffset, Paint paint)
         * hOffset 和 vOffset 是文字相对于 Path 的水平偏移量和竖直偏移量，利用它们可以调整文字的位置。
         * 例如你设置 hOffset 为 5， vOffset 为 10，文字就会右移 5 像素和下移 10 像素。
         * 记住一条原则： drawTextOnPath() 使用的 Path ，拐弯处全用圆角，别用尖角。
         *
         *
         * 如果需要绘制多行的文字，你必须自行把文字切断后分多次使用 drawText() 来绘制，或者使用 StaticLayout
         * StaticLayout(CharSequence source, TextPaint paint, int width, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad)
         * width 是文字区域的宽度，文字到达这个宽度后就会自动换行；
         * align 是文字的对齐方向；
         * spacingmult 是行间距的倍数，通常情况下填 1 就好；
         * spacingadd 是行间距的额外增加值，通常情况下填 0 就好；
         * includeadd 是指是否在文字上下添加额外的空间，来避免某些过高的字符的绘制出现越界。
         *
         * StaticLayout 并不是一个 View 或者 ViewGroup ，而是 android.text.Layout 的子类，它是纯粹用来绘制文字的。
         * StaticLayout 支持换行，它既可以为文字设置宽度上限来让文字自动换行，也会在 \n 处主动换行。
         * 如果你需要进行多行文字的绘制，并且对文字的排列和样式没有太复杂的花式要求，那么使用 StaticLayout 就好。
         */
        switch (viewType) {
            case 0:
                canvas.drawText("hello", 0, 100, paint);
                break;
            case 1:
                canvas.drawPath(path, paint);
                canvas.drawTextOnPath("aaaaaaaaaaaaaaaaa", path, 10, 0, paint);
                break;
            case 2:
                canvas.drawText(text1, 0, 100, paint);
                break;
            case 3:
                canvas.save();
                canvas.translate(50, 100);
                staticLayout1.draw(canvas);
                canvas.restore();
                break;
        }
    }

    /**
     * 绘制文字
     * drawText(String text, float x, float y, Paint paint)
     * x 和 y 是文字的坐标，这个坐标并不是文字的左上角，而是一个与左下角比较接近的位置
     * drawText() 参数中的 y，指的是文字的基线（ baseline ）的位置。
     */
    @Multiline
    static String INFO_0;

    /**
     * 沿着一条 Path 来绘制文字
     * drawTextOnPath(String text, Path path, float hOffset, float vOffset, Paint paint)
     * hOffset 和 vOffset 是文字相对于 Path 的水平偏移量和竖直偏移量，利用它们可以调整文字的位置。
     * 例如你设置 hOffset 为 5， vOffset 为 10，文字就会右移 5 像素和下移 10 像素。
     * 记住一条原则： drawTextOnPath() 使用的 Path ，拐弯处全用圆角，别用尖角。
     * paint.setStyle(Paint.Style.STROKE);
     * paint.setStrokeWidth(5);
     * paint.setTextSize(80);
     * canvas.drawPath(path, paint);
     * canvas.drawTextOnPath("aaaaaaaaaaaaaaaaa", path, 10, 0, paint)
     */
    @Multiline
    static String INFO_1;

    /**
     * 如果需要绘制多行的文字，你必须自行把文字切断后分多次使用 drawText() 来绘制，或者使用 StaticLayout。
     * StaticLayout(CharSequence source, TextPaint paint, int width, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad)
     * width 是文字区域的宽度，文字到达这个宽度后就会自动换行；
     * align 是文字的对齐方向；
     * spacingmult 是行间距的倍数，通常情况下填 1 就好；
     * spacingadd 是行间距的额外增加值，通常情况下填 0 就好；
     * includeadd 是指是否在文字上下添加额外的空间，来避免某些过高的字符的绘制出现越界。
     *
     * StaticLayout 并不是一个 View 或者 ViewGroup ，而是 android.text.Layout 的子类，它是纯粹用来绘制文字的。
     * StaticLayout 支持换行，它既可以为文字设置宽度上限来让文字自动换行，也会在 \n 处主动换行。
     * 如果你需要进行多行文字的绘制，并且对文字的排列和样式没有太复杂的花式要求，那么使用 StaticLayout 就好。
     *
     * paint.setTextSize(40);
     * canvas.drawText(text1, 0, 100, paint);
     */
    @Multiline
    static String INFO_2;

    /**
     * staticLayout1 = new StaticLayout(text1, textPaint, 600, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
     * canvas.save();
     * canvas.translate(50, 100);
     * staticLayout1.draw(canvas);
     * canvas.restore();
     */
    @Multiline
    static String INFO_3;
}
