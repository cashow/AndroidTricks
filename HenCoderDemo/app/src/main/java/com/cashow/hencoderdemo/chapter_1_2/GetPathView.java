package com.cashow.hencoderdemo.chapter_1_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

public class GetPathView extends BaseView {
    private Paint paint;
    private Path path;
    private String text;

    public GetPathView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public GetPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GetPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        path = new Path();
        text = "lalalala";
        /**
         * 计算出绘制 Path 时的实际 Path
         * getFillPath(Path src, Path dst)
         * 默认情况下（线条宽度为 0、没有 PathEffect），原 Path 和实际 Path 是一样的；
         * 而在线条宽度不为 0 （并且模式为 STROKE 模式或 FLL_AND_STROKE ），或者设置了 PathEffect 的时候，实际 Path 就和原  Path 不一样了
         * 通过 getFillPath(src, dst) 方法就能获取这个实际 Path。
         * src 是原 Path ，而 dst 就是实际 Path 的保存位置。
         * getFillPath(src, dst) 会计算出实际 Path，然后把结果保存在 dst 里。
         *
         * 计算出绘制文字时的实际 Path
         * getTextPath(String text, int start, int end, float x, float y, Path path)
         * getTextPath(char[] text, int index, int count, float x, float y, Path path)
         */
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30);
        path.rLineTo(50, 100);
        path.rLineTo(50, -100);
        path.rLineTo(70, 200);
        path.rLineTo(40, -150);
        switch (viewType) {
            case 0:
                break;
            case 1:
                PathEffect dashPathEffect1 = new DashPathEffect(new float[]{20, 10, 20, 5}, 0);
                paint.setPathEffect(dashPathEffect1);
                Path newFillPath = new Path();
                paint.getFillPath(path, newFillPath);
                break;
            case 2:
                paint = new Paint();
                paint.setTextSize(60);
                Path newTextPath = new Path();
                paint.getTextPath(text, 0, text.length(), 0, 0, newTextPath);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (viewType) {
            case 2:
                canvas.drawText(text, 0, 100, paint);
                break;
            default:
                canvas.drawPath(path, paint);
                break;
        }
    }

    @Override
    public String getViewTypeInfo(int viewType) {
        switch (viewType) {
            case 0:
                return "计算出绘制 Path 时的实际 Path\n" +
                        "getFillPath(Path src, Path dst)\n" +
                        "默认情况下（线条宽度为 0、没有 PathEffect），原 Path 和实际 Path 是一样的；\n" +
                        "而在线条宽度不为 0 （并且模式为 STROKE 模式或 FLL_AND_STROKE ），或者设置了 PathEffect 的时候，实际 Path 就和原  Path 不一样了\n" +
                        "通过 getFillPath(src, dst) 方法就能获取这个实际 Path。\n" +
                        "src 是原 Path ，而 dst 就是实际 Path 的保存位置。\n" +
                        "getFillPath(src, dst) 会计算出实际 Path，然后把结果保存在 dst 里。\n" +
                        "\n" +
                        "计算出绘制文字时的实际 Path\n" +
                        "getTextPath(String text, int start, int end, float x, float y, Path path)\n" +
                        "getTextPath(char[] text, int index, int count, float x, float y, Path path)\n\n" +
                        "canvas.drawPath(path, paint)";
            case 1:
                return "PathEffect dashPathEffect1 = new DashPathEffect(new float[]{20, 10, 20, 5}, 0);\n" +
                        "paint.setPathEffect(dashPathEffect1);\n" +
                        "Path newFillPath = new Path();\n" +
                        "paint.getFillPath(path, newFillPath);\n" +
                        "canvas.drawPath(path, paint);";
            case 2:
                return "paint = new Paint();\n" +
                        "paint.setTextSize(60);\n" +
                        "Path newTextPath = new Path();\n" +
                        "paint.getTextPath(text, 0, text.length(), 0, 0, newTextPath);\n" +
                        "canvas.drawText(text, 0, 100, paint);";
        }
        return super.getViewTypeInfo(viewType);
    }
}
