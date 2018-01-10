package com.cashow.hencoderdemo.chapter_1_3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

import java.util.Locale;

public class TextEffectView extends BaseView {
    private Paint paint;

    public TextEffectView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public TextEffectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextEffectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        /**
         * 设置文字大小
         * setTextSize(float textSize)
         *
         * 设置字体
         * setTypeface(Typeface typeface)
         *
         * 是否使用伪粗体
         * setFakeBoldText(boolean fakeBoldText)
         * 之所以叫伪粗体（ fake bold ），因为它并不是通过选用更高 weight 的字体让文字变粗，而是通过程序在运行时把文字给「描粗」了
         *
         * 是否加删除线
         * setStrikeThruText(boolean strikeThruText)
         *
         * 是否加下划线
         * setUnderlineText(boolean underlineText)
         *
         * 设置文字横向错切角度。其实就是文字倾斜度
         * setTextSkewX(float skewX)
         *
         * 设置文字横向放缩
         * setTextScaleX(float scaleX)
         *
         * 设置字符间距。默认值是 0
         * setLetterSpacing(float letterSpacing)
         *
         * 用 CSS 的 font-feature-settings 的方式来设置文字
         * setFontFeatureSettings(String settings)
         *
         * 设置文字的对齐方式
         * setTextAlign(Paint.Align align)
         * 一共有三个值：LEFT CETNER 和 RIGHT。默认值为 LEFT
         * LEFT 时文字绘制在 (x, y) 的右边
         * CETNER 时文字的中心和 (x, y) 的中心对齐
         * RIGHT 时文字绘制在 (x, y) 的左侧
         *
         * 设置绘制所使用的 Locale
         * setTextLocale(Locale locale)
         * setTextLocales(LocaleList locales)
         * Locale 直译是「地域」，其实就是你在系统里设置的「语言」或「语言区域」，比如「简体中文（中国）」「English (US)」「English (UK)」。
         * 有些同源的语言，在文化发展过程中对一些相同的字衍生出了不同的写法
         * （比如中国大陆和日本对于某些汉字的写法就有细微差别。注意，不是繁体和简体这种同音同义不同字，而真的是同样的一个字有两种写法）。
         * 系统语言不同，同样的一个字的显示就有可能不同。
         * 由于 Android 7.0 ( API v24) 加入了多语言区域的支持，所以在 API v24 以及更高版本上，还可以使用 setTextLocales(LocaleList locales) 来为绘制设置多个语言区域。
         *
         * 设置是否启用字体的 hinting （字体微调）
         * setHinting(int mode)
         * 现在的 Android 设备大多数都是是用的矢量字体。矢量字体的原理是对每个字体给出一个字形的矢量描述，
         * 然后使用这一个矢量来对所有的尺寸的字体来生成对应的字形。由于不必为所有字号都设计它们的字体形状，
         * 所以在字号较大的时候，矢量字体也能够保持字体的圆润，这是矢量字体的优势。
         * 不过当文字的尺寸过小（比如高度小于 16 像素），有些文字会由于失去过多细节而变得不太好看。
         * hinting 技术就是为了解决这种问题的：通过向字体中加入 hinting 信息，让矢量字体在尺寸过小的时候得到针对性的修正，从而提高显示效果。
         * 不过在现在，手机屏幕的像素密度已经非常高，几乎不会再出现字体尺寸小到需要靠 hinting 来修正的情况，所以这个方法其实没啥用了
         *
         * 是否开启次像素级的抗锯齿（ sub-pixel anti-aliasing ）
         * setSubpixelText(boolean subpixelText)
         * 次像素级抗锯齿简单说就是根据程序所运行的设备的屏幕类型，来进行针对性的次像素级的抗锯齿计算，从而达到更好的抗锯齿效果。
         * 不过，和前面讲的字体 hinting 一样，由于现在手机屏幕像素密度已经很高，所以默认抗锯齿效果就已经足够好了，一般没必要开启次像素级抗锯齿，所以这个方法基本上没有必要使用。
         *
         * 设置是否打开线性文本标识
         * setLinearText (boolean linearText)
         * 在Android中文本的绘制需要使用一个bitmap作为单个字符的缓存，既然是缓存必定要使用一定的空间，我们可以通过setLinearText (true)告诉Android我们不需要这样的文本缓存。
         */
        paint.setTextSize(80);
        switch (viewType) {
            case 0:
                paint.setTextSize(40);
                break;
            case 1:
                paint.setTypeface(Typeface.DEFAULT);
                break;
            case 2:
                paint.setTypeface(Typeface.SERIF);
                break;
            case 3:
                paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "RobotoCondensed-BoldItalic.ttf"));
                break;
            case 4:
                paint.setFakeBoldText(true);
                break;
            case 5:
                paint.setStrikeThruText(true);
                break;
            case 6:
                paint.setUnderlineText(true);
                break;
            case 7:
                paint.setTextSkewX(-0.5f);
                break;
            case 8:
                paint.setTextScaleX(1.5f);
                break;
            case 9:
                paint.setLetterSpacing(0.3f);
                break;
            case 10:
                paint.setFontFeatureSettings("smcp");
                break;
            case 11:
                paint.setTextAlign(Paint.Align.LEFT);
                break;
            case 12:
                paint.setTextAlign(Paint.Align.CENTER);
                break;
            case 13:
                paint.setTextAlign(Paint.Align.RIGHT);
                break;
            case 14:
                paint.setTextSize(60);
                paint.setTextLocale(Locale.CHINA);
                break;
            case 15:
                paint.setTextSize(60);
                paint.setTextLocale(Locale.TAIWAN);
                break;
            case 16:
                paint.setTextSize(60);
                paint.setTextLocale(Locale.JAPAN);
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
        switch (viewType) {
            case 11:
            case 12:
            case 13:
                canvas.drawText("Hello", 150, 100, paint);
                break;
            case 14:
            case 15:
            case 16:
                canvas.drawText("雨骨底条今", 0, 100, paint);
                break;
            default:
                canvas.drawText("Hello", 0, 100, paint);
                break;
        }
    }
}
