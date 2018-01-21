package com.cashow.hencoderdemo.chapter_1_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cashow.hencoderdemo.common.BaseView;

import org.adrianwalker.multilinestring.Multiline;

public class XferModeView extends BaseView {
    private Paint paint;
    private Xfermode xfermode;

    public XferModeView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public XferModeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public XferModeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        /**
         * setXfermode(Xfermode xfermode)
         * Xfermode 指的是你要绘制的内容和 Canvas 的目标位置的内容应该怎样结合计算出最终的颜色。
         * 但通俗地说，其实就是要你以绘制的内容作为源图像，以 View 中已有的内容作为目标图像，选取一个 PorterDuff.Mode 作为绘制内容的颜色处理方案。
         *
         * PorterDuff.Mode 在 Paint 一共有三处 API ，它们的工作原理都一样，只是用途不同：
         * ComposeShader : 混合两个 Shader
         * PorterDuffColorFilter : 增加一个单身的 ColorFilter
         * Xfermode : 设置绘制内容和 View 中已有内容的混合计算方式
         *
         * Xfermode 使用很简单，不过有两点需要注意：
         * 1. 使用离屏缓冲（Off-screen Buffer）
         * 要想使用 setXfermode() 正常绘制，必须使用离屏缓存 (Off-screen Buffer) 把内容绘制在额外的层上，再把绘制好的内容贴回 View 中。
         * 通过使用离屏缓冲，把要绘制的内容单独绘制在缓冲层， Xfermode 的使用就不会出现奇怪的结果了。使用离屏缓冲有两种方式：
         *   1. Canvas.saveLayer()
         *   saveLayer() 可以做短时的离屏缓冲。使用方法很简单，在绘制代码的前后各加一行代码，在绘制之前保存，绘制之后恢复：
         *   2. View.setLayerType()
         *   View.setLayerType() 是直接把整个 View 都绘制在离屏缓冲中。
         *   setLayerType(LAYER_TYPE_HARDWARE) 是使用 GPU 来缓冲，
         *   setLayerType(LAYER_TYPE_SOFTWARE) 是直接用一个 Bitmap 来缓冲。
         * 2. 控制好透明区域
         * 使用 Xfermode 来绘制的内容，除了注意使用离屏缓冲，还应该注意控制它的透明区域不要太小，要让它足够覆盖到要和它结合绘制的内容，否则得到的结果很可能不是你想要的。
         * 由于透明区域过小而覆盖不到的地方，将不会受到 Xfermode 的影响。
         */
        switch (viewType) {
            case 0:
                xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
                break;
            case 1:
                xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
                break;
            case 2:
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
                break;
            case 3:
                xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
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
            case 1:
            case 2:
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                paint.setXfermode(xfermode); // 设置 Xfermode
                canvas.drawBitmap(testBitmap, 0, 0, paint);
                paint.setXfermode(null); // 用完及时清除 Xfermode
                break;
            case 3:
                int savedCount = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
                canvas.drawBitmap(logoBitmap, 0, 0, paint);
                paint.setXfermode(xfermode); // 设置 Xfermode
                canvas.drawBitmap(testBitmap, 0, 0, paint);
                paint.setXfermode(null); // 用完及时清除 Xfermode
                canvas.restoreToCount(savedCount);
        }
    }

    /**
     * setXfermode(Xfermode xfermode)
     * Xfermode 指的是你要绘制的内容和 Canvas 的目标位置的内容应该怎样结合计算出最终的颜色。
     * 但通俗地说，其实就是要你以绘制的内容作为源图像，以 View 中已有的内容作为目标图像，选取一个 PorterDuff.Mode 作为绘制内容的颜色处理方案。
     *
     * PorterDuff.Mode 在 Paint 一共有三处 API ，它们的工作原理都一样，只是用途不同：
     * ComposeShader : 混合两个 Shader
     * PorterDuffColorFilter : 增加一个单身的 ColorFilter
     * Xfermode : 设置绘制内容和 View 中已有内容的混合计算方式
     *
     * Xfermode 使用很简单，不过有两点需要注意：
     * 1. 使用离屏缓冲（Off-screen Buffer）
     * 要想使用 setXfermode() 正常绘制，必须使用离屏缓存 (Off-screen Buffer) 把内容绘制在额外的层上，再把绘制好的内容贴回 View 中。
     * 通过使用离屏缓冲，把要绘制的内容单独绘制在缓冲层， Xfermode 的使用就不会出现奇怪的结果了。使用离屏缓冲有两种方式：
     *   1. Canvas.saveLayer()
     *   saveLayer() 可以做短时的离屏缓冲。使用方法很简单，在绘制代码的前后各加一行代码，在绘制之前保存，绘制之后恢复：
     *   2. View.setLayerType()
     *   View.setLayerType() 是直接把整个 View 都绘制在离屏缓冲中。
     *   setLayerType(LAYER_TYPE_HARDWARE) 是使用 GPU 来缓冲，
     *   setLayerType(LAYER_TYPE_SOFTWARE) 是直接用一个 Bitmap 来缓冲。
     * 2. 控制好透明区域
     * 使用 Xfermode 来绘制的内容，除了注意使用离屏缓冲，还应该注意控制它的透明区域不要太小，要让它足够覆盖到要和它结合绘制的内容，否则得到的结果很可能不是你想要的。
     * 由于透明区域过小而覆盖不到的地方，将不会受到 Xfermode 的影响。
     *
     * xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
     * canvas.drawBitmap(logoBitmap, 0, 0, paint)
     * paint.setXfermode(xfermode) // 设置 Xfermode
     * canvas.drawBitmap(testBitmap, 0, 0, paint)
     * paint.setXfermode(null) // 用完及时清除 Xfermode
     */
    @Multiline
    static String INFO_0;

    /**
     * xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
     * canvas.drawBitmap(logoBitmap, 0, 0, paint)
     * paint.setXfermode(xfermode) // 设置 Xfermode
     * canvas.drawBitmap(testBitmap, 0, 0, paint)
     * paint.setXfermode(null) // 用完及时清除 Xfermode
     */
    @Multiline
    static String INFO_1;

    /**
     * setLayerType(View.LAYER_TYPE_SOFTWARE, null);
     * xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
     * canvas.drawBitmap(logoBitmap, 0, 0, paint)
     * paint.setXfermode(xfermode) // 设置 Xfermode
     * canvas.drawBitmap(testBitmap, 0, 0, paint)
     * paint.setXfermode(null) // 用完及时清除 Xfermode
     */
    @Multiline
    static String INFO_2;

    /**
     * xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
     * int savedCount = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
     * canvas.drawBitmap(logoBitmap, 0, 0, paint)
     * paint.setXfermode(xfermode) // 设置 Xfermode
     * canvas.drawBitmap(testBitmap, 0, 0, paint)
     * paint.setXfermode(null) // 用完及时清除 Xfermode
     * canvas.restoreToCount(savedCount)
     */
    @Multiline
    static String INFO_3;
}
