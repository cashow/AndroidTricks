package com.cashow.hencoderdemo.chapter_1_4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

import org.adrianwalker.multilinestring.Multiline;

public class ClipView extends BaseView {
    private Paint paint;
    private Path path;

    public ClipView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public ClipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        path = new Path();
        /**
         * 范围裁切有两个方法： clipRect() 和 clipPath()。裁切方法之后的绘制代码，都会被限制在裁切范围内。
         * 裁切后记得要加上 Canvas.save() 和 Canvas.restore() 来及时恢复绘制范围
         *
         * 按照 Rect 裁切
         * clipRect(int left, int top, int right, int bottom)
         *
         * 按照 path 裁切
         * clipPath(Path path)
         */
        switch (viewType) {
            case 2:
                path.addCircle(150, 150, 100, Path.Direction.CW);
                break;
            case 3:
                path.addOval(50, 70, 250, 220, Path.Direction.CW);
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
        switch (viewType) {
            case 0:
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                break;
            case 1:
                canvas.save();
                canvas.clipRect(50, 50, 250, 250);
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                canvas.restore();
                break;
            case 2:
            case 3:
                canvas.save();
                canvas.clipPath(path);
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                canvas.restore();
                break;
        }
    }

    /**
     * 范围裁切有两个方法： clipRect() 和 clipPath()。裁切方法之后的绘制代码，都会被限制在裁切范围内。
     * 裁切后记得要加上 Canvas.save() 和 Canvas.restore() 来及时恢复绘制范围
     *
     * canvas.drawBitmap(logoBitmap, 0, 0, paint);
     */
    @Multiline
    static String INFO_0;

    /**
     * 按照 Rect 裁切
     * clipRect(int left, int top, int right, int bottom)
     *
     * canvas.save();
     * canvas.clipRect(50, 50, 250, 250);
     * canvas.drawBitmap(logoBitmap, 0, 0, paint);
     * canvas.restore();
     */
    @Multiline
    static String INFO_1;

    /**
     * 按照 path 裁切
     * clipPath(Path path)
     *
     * path.addCircle(150, 150, 100, Path.Direction.CW);
     * canvas.save();
     * canvas.clipPath(path);
     * canvas.drawBitmap(logoBitmap, 0, 0, paint);
     * canvas.restore();
     */
    @Multiline
    static String INFO_2;

    /**
     * path.addOval(50, 70, 250, 220, Path.Direction.CW);
     * canvas.save();
     * canvas.clipPath(path);
     * canvas.drawBitmap(logoBitmap, 0, 0, paint);
     * canvas.restore();
     */
    @Multiline
    static String INFO_3;
}
