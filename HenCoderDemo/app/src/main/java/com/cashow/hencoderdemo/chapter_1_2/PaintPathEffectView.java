package com.cashow.hencoderdemo.chapter_1_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
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
         * DiscretePathEffect(float segmentLength, float deviation)
         * DiscretePathEffect 具体的做法是，把绘制改为使用定长的线段来拼接，并且在拼接的时候对路径进行随机偏离。
         * segmentLength 是用来拼接的每个线段的长度，
         * deviation 是偏离量。
         *
         * 使用虚线来绘制线条
         * DashPathEffect(float[] intervals, float phase)
         * 第一个参数 intervals 是一个数组，它指定了虚线的格式：
         * 数组中元素必须为偶数（最少是 2 个），按照「画线长度、空白长度、画线长度、空白长度」……的顺序排列，
         * 例如上面代码中的 20, 5, 10, 5 就表示虚线是按照「画 20 像素、空 5 像素、画 10 像素、空 5 像素」的模式来绘制；
         * 第二个参数 phase 是虚线的偏移量。
         *
         * 使用一个 Path 来绘制虚线
         * PathDashPathEffect(Path shape, float advance, float phase, PathDashPathEffect.Style style)
         * shape 参数是用来绘制的 Path ；
         * advance 是两个相邻的 shape 段之间的间隔，不过注意，这个间隔是两个 shape 段的起点的间隔，而不是前一个的终点和后一个的起点的距离；
         * phase 和  DashPathEffect 中一样，是虚线的偏移；
         * style，是用来指定拐弯改变的时候 shape 的转换方式。style 的类型为 PathDashPathEffect.Style ，是一个 enum ，具体有三个值：
         * TRANSLATE：位移
         * ROTATE：旋转
         * MORPH：变体
         *
         * 组合效果类的 PathEffect
         * ComposePathEffect
         * ComposePathEffect(PathEffect outerpe, PathEffect innerpe)
         * 先对目标 Path 使用一个 PathEffect，然后再对这个改变后的 Path 使用另一个 PathEffect。
         * innerpe 是先应用的， outerpe 是后应用的。
         *
         * 注意： PathEffect 在有些情况下不支持硬件加速，需要关闭硬件加速才能正常使用：
         * 1. Canvas.drawLine() 和 Canvas.drawLines() 方法画直线时，setPathEffect() 是不支持硬件加速的；
         * 2. PathDashPathEffect 对硬件加速的支持也有问题，所以当使用 PathDashPathEffect 的时候，最好也把硬件加速关了。
         *
         * 在之后的绘制内容下面加一层阴影
         * setShadowLayer(float radius, float dx, float dy, int shadowColor)
         * radius 是阴影的模糊范围；
         * dx dy 是阴影的偏移量；
         * shadowColor 是阴影的颜色。
         * 如果要清除阴影层，使用 clearShadowLayer() 。
         * 在硬件加速开启的情况下， setShadowLayer() 只支持文字的绘制，文字之外的绘制必须关闭硬件加速才能正常绘制阴影。
         * 如果 shadowColor 是半透明的，阴影的透明度就使用 shadowColor 自己的透明度；而如果  shadowColor 是不透明的，阴影的透明度就使用 paint 的透明度。
         */
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        path.rLineTo(50, 100);
        path.rLineTo(50, -100);
        path.rLineTo(70, 200);
        path.rLineTo(40, -150);
        int dashWidth = 40;
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
            case 5:
                PathEffect dashPathEffect1 = new DashPathEffect(new float[]{20, 10, 20, 5}, 0);
                paint.setPathEffect(dashPathEffect1);
                break;
            case 6:
                PathEffect dashPathEffect2 = new DashPathEffect(new float[]{20, 10, 20, 5}, 10);
                paint.setPathEffect(dashPathEffect2);
                break;
            case 7:
                Path dashPath1 = new Path();
                dashPath1.rLineTo(dashWidth, 0);
                dashPath1.rLineTo(-dashWidth / 2, dashWidth);
                dashPath1.rLineTo(-dashWidth / 2, -dashWidth);
                path = new Path();
                path.addOval(0, 0, 250, 200, Path.Direction.CW);
                PathEffect pathDashPathEffect1 = new PathDashPathEffect(dashPath1, 40, 0, PathDashPathEffect.Style.TRANSLATE);
                paint.setPathEffect(pathDashPathEffect1);
                break;
            case 8:
                Path dashPath2 = new Path();
                dashPath2.rLineTo(dashWidth, 0);
                dashPath2.rLineTo(-dashWidth / 2, dashWidth);
                dashPath2.rLineTo(-dashWidth / 2, -dashWidth);
                path = new Path();
                path.addOval(0, 0, 250, 200, Path.Direction.CW);
                PathEffect pathDashPathEffect2 = new PathDashPathEffect(dashPath2, 40, 0, PathDashPathEffect.Style.ROTATE);
                paint.setPathEffect(pathDashPathEffect2);
                break;
            case 9:
                Path dashPath3 = new Path();
                dashPath3.rLineTo(dashWidth, 0);
                dashPath3.rLineTo(-dashWidth / 2, dashWidth);
                dashPath3.rLineTo(-dashWidth / 2, -dashWidth);
                path = new Path();
                path.addOval(0, 0, 250, 200, Path.Direction.CW);
                PathEffect pathDashPathEffect3 = new PathDashPathEffect(dashPath3, 40, 0, PathDashPathEffect.Style.MORPH);
                paint.setPathEffect(pathDashPathEffect3);
                break;
            case 10:
                PathEffect pathEffect1 = new DashPathEffect(new float[]{20, 10, 20, 5}, 10);
                PathEffect pathEffect2 = new DiscretePathEffect(20, 5);
                PathEffect composePathEffect = new ComposePathEffect(pathEffect1, pathEffect2);
                paint.setPathEffect(composePathEffect);
                break;
            case 11:
                paint = new Paint();
                paint.setTextSize(60);
                paint.setShadowLayer(10, 0, 0, Color.RED);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 12;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (viewType) {
            case 11:
                canvas.drawText("lalalala", 0, 100, paint);
                break;
            default:
                canvas.drawPath(path, paint);
                break;
        }
    }
}
