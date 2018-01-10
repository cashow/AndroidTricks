package com.cashow.hencoderdemo.chapter_1_3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cashow.hencoderdemo.common.BaseView;
import com.cashow.hencoderdemo.common.MLog;

public class TextMeasureView extends BaseView {
    private Paint paint;
    private String text;
    private String breakText;
    private Rect bounds;
    private int offsetY;
    private float[] widths;
    private float[] breakTextWidths;
    private Float currentX;
    private Float currentY;

    public TextMeasureView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public TextMeasureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextMeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        text = "Hello";
        breakText = "HelloWorld";
        bounds = new Rect();
        widths = new float[text.length()];
        breakTextWidths = new float[breakText.length()];
        offsetY = 100;
        paint.setTextSize(60);
        paint.setStyle(Paint.Style.STROKE);
        /**
         * 获取推荐的行距。
         * float getFontSpacing()
         * 推荐两行文字的 baseline 的距离。这个值是系统根据文字的字体和字号自动计算的。它的作用是当你要手动绘制多行文字（而不是使用 StaticLayout）的时候，可以在换行的时候给 y 坐标加上这个值来下移文字。
         *
         *
         * 获取 Paint 的 FontMetrics
         * FontMetircs getFontMetrics()
         * FontMetrics 是个相对专业的工具类，它提供了几个文字排印方面的数值：ascent, descent, top,  bottom, leading。
         * baseline: 它的作用是作为文字显示的基准线。
         * ascent / descent: 它们的作用是限制普通字符的顶部和底部范围。普通的字符，上不会高过 ascent ，下不会低过 descent
         *   具体到 Android 的绘制中，ascent 的值是和 baseline 的相对位移，它的值为负（因为它在 baseline 的上方）；
         *   descent 的值是和 baseline 相对位移，值为正（因为它在 baseline 的下方）。
         * top / bottom: 它们的作用是限制所有字形（ glyph ）的顶部和底部范围。
         *   除了普通字符，有些字形的显示范围是会超过 ascent 和 descent 的，
         *   而 top 和 bottom 则限制的是所有字形的显示范围，包括这些特殊字形。
         *   具体到 Android 的绘制中，top 的值是和 baseline 的相对位移，它的值为负（因为它在 baseline 的上方）；
         *   bottom 的值是和 baseline 相对位移，值为正（因为它在 baseline 的下方）。
         * leading: leading 指的是行的额外间距，即对于上下相邻的两行，上行的 bottom 线和下行的 top 线的距离。
         *   leading 这个词的本意其实并不是行的额外间距，而是行距，即两个相邻行的 baseline 之间的距离。
         *   不过对于很多非专业领域，leading 的意思被改变了，被大家当做行的额外间距来用；
         *   而 Android 里的 leading ，同样也是行的额外间距的意思。
         *
         * FontMetrics 提供的就是 Paint 根据当前字体和字号，得出的这些值的推荐值。它把这些值以变量的形式存储，供开发者需要时使用。
         *   FontMetrics.ascent：float 类型。
         *   FontMetrics.descent：float 类型。
         *   FontMetrics.top：float 类型。
         *   FontMetrics.bottom：float 类型。
         *   FontMetrics.leading：float 类型。
         *
         * 另外，ascent 和 descent 这两个值还可以通过 Paint.ascent() 和 Paint.descent() 来快捷获取。
         *
         * 关于 FontMetrics 和 getFontSpacing()：
         * 从定义可以看出，两行文字的 font spacing (即相邻两行的 baseline 的距离) 可以通过  bottom - top + leading (top 的值为负）来计算得出。
         * 但你真的运行一下会发现， bottom - top + leading 的结果是要大于 getFontSpacing() 的返回值的。
         * 这并不是 bug，而是因为 getFontSpacing() 的结果并不是通过 FontMetrics 的标准值计算出来的，
         * 而是另外计算出来的一个值，它能够做到在两行文字不显得拥挤的前提下缩短行距，以此来得到更好的显示效果。
         * 所以如果你要对文字手动换行绘制，多数时候应该选取 getFontSpacing() 来得到行距，不但使用更简单，显示效果也会更好。
         *
         * getFontMetrics() 的返回值是 FontMetrics 类型。
         * 它还有一个重载方法  getFontMetrics(FontMetrics fontMetrics) ，
         * 计算结果会直接填进传入的 FontMetrics 对象，而不是重新创建一个对象。
         * 这种用法在需要频繁获取 FontMetrics 的时候性能会好些。
         *
         * 另外，这两个方法还有一对同样结构的对应的方法 getFontMetricsInt() 和  getFontMetricsInt(FontMetricsInt fontMetrics) ，用于获取 FontMetricsInt 类型的结果。
         *
         *
         * 获取文字的显示范围
         * getTextBounds(String text, int start, int end, Rect bounds)
         *
         * 测量文字的宽度并返回
         * float measureText(String text)
         * 如果你用代码分别使用 getTextBounds() 和 measureText() 来测量文字的宽度，
         *
         * 你会发现  measureText() 测出来的宽度总是比 getTextBounds() 大一点点。
         * 这是因为这两个方法其实测量的是两个不一样的东西。
         * getTextBounds: 它测量的是文字的显示范围（关键词：显示）。
         * 形象点来说，你这段文字外放置一个可变的矩形，然后把矩形尽可能地缩小，一直小到这个矩形恰好紧紧包裹住文字，
         * 那么这个矩形的范围，就是这段文字的 bounds。
         * measureText(): 它测量的是文字绘制时所占用的宽度（关键词：占用）。
         * 一个文字在界面中，往往需要占用比他的实际显示宽度更多一点的宽度，以此来让文字和文字之间保留一些间距，不会显得过于拥挤。
         * 在没有设置 setLetterSpacing() 的情况下，letter spacing 是默认值 0，但可以看到，每两个字母之间都是有空隙的。
         * 另外，表示文字宽度的横线，在左边超出了第一个字母一段距离的，在右边也超出了最后一个字母，
         * 而就是两边的这两个「超出」，导致了 measureText() 比 getTextBounds() 测量出的宽度要大一些。
         * 在实际的开发中，测量宽度要用 measureText() 还是 getTextBounds() ，需要根据情况而定。
         *
         * 获取字符串中每个字符的宽度，并把结果填入参数 widths。
         * getTextWidths(String text, float[] widths)
         * 这相当于 measureText() 的一个快捷方法，它的计算等价于对字符串中的每个字符分别调用  measureText() ，并把它们的计算结果分别填入 widths 的不同元素。
         *
         *
         * int breakText(String text, boolean measureForwards, float maxWidth, float[] measuredWidth)
         * text 是要测量的文字；
         * measureForwards 表示文字的测量方向，true 表示由左往右测量；
         * maxWidth 是给出的宽度上限；
         * measuredWidth 是用于接受数据，而不是用于提供数据的：方法测量完成后会把截取的文字宽度（如果宽度没有超限，则为文字总宽度）赋值给 measuredWidth[0]。
         *
         * 这个方法也是用来测量文字宽度的。但和 measureText() 的区别是，breakText() 是在给出宽度上限的前提下测量文字的宽度。如果文字的宽度超出了上限，那么在临近超限的位置截断文字。
         * breakText() 的返回值是截取的文字个数（如果宽度没有超限，则是文字的总个数）。
         * 这个方法可以用于多行文字的折行计算。
         *
         * 对于一段文字，计算出某个字符处光标的 x 坐标。
         * getRunAdvance(CharSequence text, int start, int end, int contextStart, int contextEnd, boolean isRtl, int offset)
         * start end 是文字的起始和结束坐标；
         * contextStart contextEnd 是上下文的起始和结束坐标；
         * isRtl 是文字的方向；
         * offset 是字数的偏移，即计算第几个字符处的光标。
         * 其实，说是测量光标位置的，本质上这也是一个测量文字宽度的方法。
         * 以下例子中，start 和  contextStart 都是 0， end contextEnd 和 offset 都等于 text.length()。
         * 在这种情况下，它是等价于 measureText(text) 的，即完整测量一段文字的宽度。
         * 而对于更复杂的需求，getRunAdvance() 能做的事就比 measureText() 多了。
         * 例如，当有文字占了 4 个字符（例如emoji）时，如果 offset 是表情中间处时，getRunAdvance() 得出的结果并不会在表情的中间处。
         *
         * 给出一个位置的像素值，计算出文字中最接近这个位置的字符偏移量（即第几个字符最接近这个坐标）
         * getOffsetForAdvance(CharSequence text, int start, int end, int contextStart, int contextEnd, boolean isRtl, float advance)
         * text 是要测量的文字；
         * start end 是文字的起始和结束坐标；
         * contextStart contextEnd 是上下文的起始和结束坐标；
         * isRtl 是文字方向；
         * advance 是给出的位置的像素值。
         *
         * getOffsetForAdvance() 配合上 getRunAdvance() 一起使用，就可以实现「获取用户点击处的文字坐标」的需求。
         *
         *
         * 检查指定的字符串中是否是一个单独的字形 (glyph）
         * hasGlyph(String string)
         */
        // 打印出来的log：getFontMetrics : ascent = -55.664063, descent = 14.648438, top = -63.369141,  bottom = 16.259766, leading = 0.000000
        MLog.d(String.format("getFontMetrics : ascent = %f, descent = %f, top = %f,  bottom = %f, leading = %f",
                paint.getFontMetrics().ascent, paint.getFontMetrics().descent,
                paint.getFontMetrics().top, paint.getFontMetrics().bottom,
                paint.getFontMetrics().leading));
        // 打印出来的log：getFontMetricsInt : ascent = -56, descent = 15, top = -64,  bottom = 17, leading = 0
        MLog.d(String.format("getFontMetricsInt : ascent = %d, descent = %d, top = %d,  bottom = %d, leading = %d",
                paint.getFontMetricsInt().ascent, paint.getFontMetricsInt().descent,
                paint.getFontMetricsInt().top, paint.getFontMetricsInt().bottom,
                paint.getFontMetricsInt().leading));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 打印出来的log： hasGlyph a : true
            MLog.d("hasGlyph a : " + paint.hasGlyph("a"));
            // 打印出来的log： hasGlyph ab : false
            MLog.d("hasGlyph ab : " + paint.hasGlyph("ab"));
            // 打印出来的log： hasGlyph 🤪 : false
            MLog.d("hasGlyph 🤪 : " + paint.hasGlyph("🤪"));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        getParent().requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            currentX = event.getX();
            currentY = event.getY();
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (viewType) {
            case 0:
                canvas.drawText("Hello", 0, offsetY, paint);
                canvas.drawText("World", 0, offsetY + paint.getFontSpacing(), paint);
                break;
            case 1:
                canvas.drawText(text, 0, offsetY, paint);
                paint.getTextBounds(text, 0, text.length(), bounds);
                bounds.top += offsetY;
                bounds.bottom += offsetY;
                canvas.drawRect(bounds, paint);
                // 打印出来的log： bounds : 5, 56, 136, 101
                MLog.d(String.format("bounds : %d, %d, %d, %d", bounds.left, bounds.top, bounds.right, bounds.bottom));
                float textWidth = paint.measureText(text);
                // 打印出来的log： textWidth : 139.000000
                MLog.d(String.format("textWidth : %f", textWidth));
                paint.getTextWidths(text, widths);
                // 打印出来的log： [42.0,32.0,15.0,15.0,35.0]
                MLog.d("widths", widths);
                break;
            case 2:
                int measuredCount2 = paint.breakText(breakText, 0, breakText.length(), true, 300, breakTextWidths);
                // 打印出来的log： [295.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
                MLog.d("breakTextWidths2", breakTextWidths);
                canvas.drawText(breakText, 0, measuredCount2, 0, offsetY, paint);
                break;
            case 3:
                int measuredCount3 = paint.breakText(breakText, 0, breakText.length(), true, 100, breakTextWidths);
                // 打印出来的log： [89.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
                MLog.d("breakTextWidths3", breakTextWidths);
                canvas.drawText(breakText, 0, measuredCount3, 0, offsetY, paint);
                break;
            case 4:
                canvas.drawText(text, 0, offsetY, paint);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    float advance = paint.getRunAdvance(text, 0, text.length(), 0, text.length(), false, text.length());
                    canvas.drawLine(advance, offsetY - 50, advance, offsetY + 10, paint);
                }
                break;
            case 5:
                canvas.drawText(text, 0, offsetY, paint);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && currentX != null) {
                    int offsetForAdvance = paint.getOffsetForAdvance(text, 0, text.length(), 0, text.length(), false, currentX);
                    float advance = paint.getRunAdvance(text, 0, text.length(), 0, text.length(), false, offsetForAdvance);
                    canvas.drawLine(advance, offsetY - 50, advance, offsetY + 10, paint);
                }
                break;
        }
    }
}
