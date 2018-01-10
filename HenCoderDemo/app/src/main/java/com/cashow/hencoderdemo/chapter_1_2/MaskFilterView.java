package com.cashow.hencoderdemo.chapter_1_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cashow.hencoderdemo.R;
import com.cashow.hencoderdemo.common.BaseView;
import com.cashow.hencoderdemo.common.BitmapUtils;

public class MaskFilterView extends BaseView {
    private Paint paint;
    private Bitmap bitmap;

    public MaskFilterView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public MaskFilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MaskFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        /**
         * 为之后的绘制设置 MaskFilter
         * setMaskFilter(MaskFilter maskfilter)
         * setMaskFilter 设置的是在绘制层上方的附加效果
         * MaskFilter 有两种： BlurMaskFilter 和 EmbossMaskFilter。
         *
         * 模糊效果的 MaskFilter
         * BlurMaskFilter(float radius, BlurMaskFilter.Blur style)
         * radius 参数是模糊的范围，
         * style 是模糊的类型。一共有四种：
         * NORMAL: 内外都模糊绘制
         * SOLID: 内部正常绘制，外部模糊
         * INNER: 内部模糊，外部不绘制
         * OUTER: 内部不绘制，外部模糊
         *
         * 浮雕效果的 MaskFilter
         * EmbossMaskFilter(float[] direction, float ambient, float specular, float blurRadius)
         * direction 是一个 3 个元素的数组，指定了光源的方向；
         * ambient 是环境光的强度，数值范围是 0 到 1；
         * specular 是炫光的系数；
         * blurRadius 是应用光线的范围。
         */
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        bitmap = BitmapUtils.drawableToBitmap(context, R.drawable.test3);
        switch (viewType) {
            case 0:
                break;
            case 1:
                paint.setMaskFilter(new BlurMaskFilter(100, BlurMaskFilter.Blur.NORMAL));
                break;
            case 2:
                paint.setMaskFilter(new BlurMaskFilter(100, BlurMaskFilter.Blur.SOLID));
                break;
            case 3:
                paint.setMaskFilter(new BlurMaskFilter(100, BlurMaskFilter.Blur.INNER));
                break;
            case 4:
                paint.setMaskFilter(new BlurMaskFilter(100, BlurMaskFilter.Blur.OUTER));
                break;
            case 5:
                paint.setMaskFilter(new EmbossMaskFilter(new float[]{0.0f, 0.5f, 1.0f}, 0.2f, 8, 10));
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
        canvas.drawBitmap(bitmap, 50, 50, paint);
    }
}
