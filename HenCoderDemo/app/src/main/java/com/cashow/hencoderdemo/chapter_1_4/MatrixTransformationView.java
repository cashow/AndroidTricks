package com.cashow.hencoderdemo.chapter_1_4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;
import com.cashow.hencoderdemo.common.BitmapUtils;

public class MatrixTransformationView extends BaseView {
    private Paint paint;
    private Matrix matrix;
    private float[] src;
    private float[] dst;

    public MatrixTransformationView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public MatrixTransformationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MatrixTransformationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        matrix = new Matrix();
        switch (viewType) {
            case 4:
                logoBitmap = BitmapUtils.getResizedBitmap(logoBitmap, 100, 100);
                break;
            case 5:
                src = new float[]{0, 0,   // 左上
                        logoWidth, 0,  // 右上
                        logoWidth, logoHeight,  // 右下
                        0, logoHeight  // 左下
                };
                dst = new float[]{100, 0,   // 左上
                        logoWidth - 100, 0,  // 右上
                        logoWidth, logoHeight - 100,  // 右下
                        0, logoHeight - 100  // 左下
                };
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 6;
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
         * 使用 Matrix 来做常见变换:
         * Matrix 做常见变换的方式：
         * 1. 创建 Matrix 对象；
         * 2. 调用 Matrix 的 pre/postTranslate/Rotate/Scale/Skew() 方法来设置几何变换；
         * 3. 使用 Canvas.setMatrix(matrix) 或 Canvas.concat(matrix) 来把几何变换应用到 Canvas。
         *
         * 把 Matrix 应用到 Canvas 有两个方法： Canvas.setMatrix(matrix) 和 Canvas.concat(matrix)。
         *   Canvas.setMatrix(matrix)：用 Matrix 直接替换 Canvas 当前的变换矩阵，即抛弃 Canvas 当前的变换，
         *   改用 Matrix 的变换（注：不同的系统中 setMatrix(matrix) 的行为可能不一致，所以还是尽量用 concat(matrix) 吧）；
         *   Canvas.concat(matrix)：用 Canvas 当前的变换矩阵和 Matrix 相乘，即基于 Canvas 当前的变换，叠加上 Matrix 中的变换。
         *
         *
         * 用点对点映射的方式设置变换
         * Matrix.setPolyToPoly(float[] src, int srcIndex, float[] dst, int dstIndex, int pointCount)
         * src 和 dst 是源点集合目标点集；srcIndex 和 dstIndex 是第一个点的偏移；pointCount 是采集的点的个数（个数不能大于 4，因为大于 4 个点就无法计算变换了）。
         * poly 就是「多」的意思。setPolyToPoly() 的作用是通过多点的映射的方式来直接设置变换。
         * 「多点映射」的意思就是把指定的点移动到给出的位置，从而发生形变。
         * 例如：(0, 0) -> (100, 100) 表示把 (0, 0) 位置的像素移动到 (100, 100) 的位置，这个是单点的映射，单点映射可以实现平移。
         * 而多点的映射，就可以让绘制内容任意地扭曲。
         */
        canvas.save();
        switch (viewType) {
            case 0:
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                break;
            case 1:
                matrix.postTranslate(50, 50);
                canvas.setMatrix(matrix);
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                break;
            case 2:
                matrix.postRotate(90, logoWidth / 2, logoHeight / 2);
                canvas.setMatrix(matrix);
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                break;
            case 3:
                matrix.postScale(0.5f, 0.5f, logoWidth / 2, logoHeight / 2);
                canvas.setMatrix(matrix);
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                break;
            case 4:
                matrix.postSkew(-0.5f, 0f);
                canvas.setMatrix(matrix);
                canvas.drawBitmap(logoBitmap, 100, 100, paint);
                break;
            case 5:
                matrix.setPolyToPoly(src, 0, dst, 0, src.length / 2);
                canvas.setMatrix(matrix);
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                break;
        }
        canvas.restore();
    }
}
