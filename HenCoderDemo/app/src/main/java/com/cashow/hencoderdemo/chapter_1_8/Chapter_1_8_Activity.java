package com.cashow.hencoderdemo.chapter_1_8;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cashow.hencoderdemo.common.BaseActivity;

import org.adrianwalker.multilinestring.Multiline;

public class Chapter_1_8_Activity extends BaseActivity {
    /**
     * 在硬件加速关闭的时候，Canvas 绘制的工作方式是：把要绘制的内容写进一个 Bitmap，然后在之后的渲染过程中，
     * 这个 Bitmap 的像素内容被直接用于渲染到屏幕。这种绘制方式的主要计算工作在于把绘制操作转换为像素的过程
     * （例如由一句 Canvas.drawCircle() 来获得一个具体的圆的像素信息），这个过程的计算是由 CPU 来完成的。
     *
     * 而在硬件加速开启时，Canvas 的工作方式改变了：它只是把绘制的内容转换为 GPU 的操作保存了下来，
     * 然后就把它交给 GPU，最终由 GPU 来完成实际的显示工作
     *
     * 硬件加速能够让绘制变快，主要有三个原因：
     *
     * 1. 本来由 CPU 自己来做的事，分摊给了 GPU 一部分，自然可以提高效率；
     * 2. 相对于 CPU 来说，GPU 自身的设计本来就对于很多常见类型内容的计算（例如简单的圆形、简单的方形）具有优势；
     * 3. 由于绘制流程的不同，硬件加速在界面内容发生重绘的时候绘制流程可以得到优化，避免了一些重复操作，从而大幅提升绘制效率。
     *
     * 硬件加速不仅是由于 GPU 的引入而提高了绘制效率，还由于绘制机制的改变，而极大地提高了界面内容改变时的刷新效率。
     * 所以把上面的三条压缩总结一下，硬件加速更快的原因有两条：
     * 1. 用了 GPU，绘制变快了；
     * 2. 绘制机制的改变，导致界面内容改变时的刷新效率极大提高。
     *
     * 硬件加速不只是好处，也有它的限制：
     * 受到 GPU 绘制方式的限制，Canvas 的有些方法在硬件加速开启式会失效或无法正常工作。
     * 比如，在硬件加速开启时， clipPath() 在 API 18 及以上的系统中才有效。
     * 具体的 API 限制：https://developer.android.google.cn/guide/topics/graphics/hardware-accel.html#unsupported
     *
     * 关闭的方式是通过这行代码
     * view.setLayerType(LAYER_TYPE_SOFTWARE, null);
     * 这个方法的本来作用并不是用来开关硬件加速的，只是当它的参数为 LAYER_TYPE_SOFTWARE 的时候，
     * 可以「顺便」把硬件加速关掉而已；并且除了这个方法之外，Android 并没有提供专门的 View 级别的硬件加速开关，
     * 所以它就「顺便」成了一个开关硬件加速的方法。
     *
     * setLayerType() 这个方法，它的作用其实就是名字里的意思：设置 View Layer 的类型。
     * 所谓 View Layer，又称为离屏缓冲（Off-screen Buffer），它的作用是单独启用一块地方来绘制这个 View ，
     * 而不是使用软件绘制的 Bitmap 或者通过硬件加速的 GPU。
     * 这块「地方」可能是一块单独的 Bitmap，也可能是一块 OpenGL 的纹理（texture，OpenGL 的纹理可以简单理解为图像的意思），
     * 具体取决于硬件加速是否开启。采用什么来绘制 View 不是关键，关键在于当设置了 View Layer 的时候，
     * 它的绘制会被缓存下来，而且缓存的是最终的绘制结果，而不是像硬件加速那样只是把 GPU 的操作保存下来再交给 GPU 去计算。
     * 通过这样更进一步的缓存方式，View 的重绘效率进一步提高了：只要绘制的内容没有变，那么不论是 CPU 绘制还是 GPU 绘制，
     * 它们都不用重新计算，而只要只用之前缓存的绘制结果就可以了。
     *
     * 基于这样的原理，在进行移动、旋转等（无需调用 invalidate()）的属性动画的时候开启 Hardware Layer 将会极大地提升动画的效率，
     * 因为在动画过程中 View 本身并没有发生改变，只是它的位置或角度改变了，而这种改变是可以由 GPU 通过简单计算就完成的，
     * 并不需要重绘整个 View。所以在这种动画的过程中开启 Hardware Layer，可以让本来就依靠硬件加速而变流畅了的动画变得更加流畅。
     *
     * 不过一定要注意，只有你在对 translationX translationY rotation alpha 等无需调用 invalidate() 的属性做动画的时候，这种方法才适用，
     * 因为这种方法本身利用的就是当界面不发生时，缓存未更新所带来的时间的节省。所以简单地说 —— 这种方式不适用于基于自定义属性绘制的动画。
     *
     * 除了用于关闭硬件加速和辅助属性动画这两项功能外，Layer 还可以用于给 View 增加一些绘制效果，例如设置一个 ColorMatrixColorFilter 来让 View 变成黑白的
     *
     * 另外，由于设置了 View Layer 后，View 在初次绘制时以及每次 invalidate() 后重绘时，需要进行两次的绘制工作（一次绘制到 Layer，一次从 Layer 绘制到显示屏），
     * 所以其实它的每次绘制的效率是被降低了的。所以一定要慎重使用 View Layer，在需要用到它的时候再去使用。
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMyDemoView(new HardwareRotate1View(context), INFO_0);
        addMyDemoView(new HardwareRotate2View(context), INFO_1);
        addMyDemoView(new HardwareLayerPaintView(context), INFO_2);
    }

    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{};
    }

    /**
     * 在硬件加速关闭的时候，Canvas 绘制的工作方式是：把要绘制的内容写进一个 Bitmap，然后在之后的渲染过程中，这个 Bitmap 的像素内容被直接用于渲染到屏幕。这种绘制方式的主要计算工作在于把绘制操作转换为像素的过程
     * （例如由一句 Canvas.drawCircle() 来获得一个具体的圆的像素信息），这个过程的计算是由 CPU 来完成的。
     *
     * 而在硬件加速开启时，Canvas 的工作方式改变了：它只是把绘制的内容转换为 GPU 的操作保存了下来，然后就把它交给 GPU，最终由 GPU 来完成实际的显示工作
     *
     * 硬件加速能够让绘制变快，主要有三个原因：
     *
     * 1. 本来由 CPU 自己来做的事，分摊给了 GPU 一部分，自然可以提高效率；
     * 2. 相对于 CPU 来说，GPU 自身的设计本来就对于很多常见类型内容的计算（例如简单的圆形、简单的方形）具有优势；
     * 3. 由于绘制流程的不同，硬件加速在界面内容发生重绘的时候绘制流程可以得到优化，避免了一些重复操作，从而大幅提升绘制效率。
     *
     * 硬件加速不仅是由于 GPU 的引入而提高了绘制效率，还由于绘制机制的改变，而极大地提高了界面内容改变时的刷新效率。
     * 所以把上面的三条压缩总结一下，硬件加速更快的原因有两条：
     * 1. 用了 GPU，绘制变快了；
     * 2. 绘制机制的改变，导致界面内容改变时的刷新效率极大提高。
     *
     * 硬件加速不只是好处，也有它的限制：
     * 受到 GPU 绘制方式的限制，Canvas 的有些方法在硬件加速开启式会失效或无法正常工作。
     * 比如，在硬件加速开启时， clipPath() 在 API 18 及以上的系统中才有效。
     * 具体的 API 限制：https://developer.android.google.cn/guide/topics/graphics/hardware-accel.html#unsupported
     *
     * 关闭的方式是通过这行代码
     * view.setLayerType(LAYER_TYPE_SOFTWARE, null);
     * 这个方法的本来作用并不是用来开关硬件加速的，只是当它的参数为 LAYER_TYPE_SOFTWARE 的时候，可以「顺便」把硬件加速关掉而已；并且除了这个方法之外，Android 并没有提供专门的 View 级别的硬件加速开关，所以它就「顺便」成了一个开关硬件加速的方法。
     *
     * setLayerType() 这个方法，它的作用其实就是名字里的意思：设置 View Layer 的类型。
     * 所谓 View Layer，又称为离屏缓冲（Off-screen Buffer），它的作用是单独启用一块地方来绘制这个 View ，
     * 而不是使用软件绘制的 Bitmap 或者通过硬件加速的 GPU。
     * 这块「地方」可能是一块单独的 Bitmap，也可能是一块 OpenGL 的纹理（texture，OpenGL 的纹理可以简单理解为图像的意思），
     * 具体取决于硬件加速是否开启。采用什么来绘制 View 不是关键，关键在于当设置了 View Layer 的时候，
     * 它的绘制会被缓存下来，而且缓存的是最终的绘制结果，而不是像硬件加速那样只是把 GPU 的操作保存下来再交给 GPU 去计算。
     * 通过这样更进一步的缓存方式，View 的重绘效率进一步提高了：只要绘制的内容没有变，那么不论是 CPU 绘制还是 GPU 绘制，
     * 它们都不用重新计算，而只要只用之前缓存的绘制结果就可以了。
     *
     * 基于这样的原理，在进行移动、旋转等（无需调用 invalidate()）的属性动画的时候开启 Hardware Layer 将会极大地提升动画的效率，
     * 因为在动画过程中 View 本身并没有发生改变，只是它的位置或角度改变了，而这种改变是可以由 GPU 通过简单计算就完成的，
     * 并不需要重绘整个 View。所以在这种动画的过程中开启 Hardware Layer，可以让本来就依靠硬件加速而变流畅了的动画变得更加流畅。
     *
     * 不过一定要注意，只有你在对 translationX translationY rotation alpha 等无需调用 invalidate() 的属性做动画的时候，这种方法才适用，
     * 因为这种方法本身利用的就是当界面不发生时，缓存未更新所带来的时间的节省。所以简单地说 —— 这种方式不适用于基于自定义属性绘制的动画。
     *
     * 除了用于关闭硬件加速和辅助属性动画这两项功能外，Layer 还可以用于给 View 增加一些绘制效果，例如设置一个 ColorMatrixColorFilter 来让 View 变成黑白的
     *
     * 另外，由于设置了 View Layer 后，View 在初次绘制时以及每次 invalidate() 后重绘时，需要进行两次的绘制工作（一次绘制到 Layer，一次从 Layer 绘制到显示屏），
     * 所以其实它的每次绘制的效率是被降低了的。所以一定要慎重使用 View Layer，在需要用到它的时候再去使用。
     *
     * setLayerType(LAYER_TYPE_HARDWARE, null);
     * ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "rotationY", 0, 180);
     * objectAnimator.addListener(new AnimatorListenerAdapter() {
     *     @Override
     *     public void onAnimationEnd(Animator animation) {
     *         super.onAnimationEnd(animation);
     *         setLayerType(LAYER_TYPE_NONE, null);
     *         postDelayed(() -> startAnim(), 1000);
     *     }
     * });
     * objectAnimator.start();
     */
    @Multiline
    static String INFO_0;

    /**
     * withLayer() 可以自动完成上面的复杂操作
     * animate().rotationYBy(180).withLayer().withEndAction(() -> postDelayed(() -> startAnim(), 1000));
     */
    @Multiline
    static String INFO_1;

    /**
     * colorMatrix = new ColorMatrix();
     * colorMatrix.setSaturation(0);
     *
     * paint = new Paint();
     * paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
     *
     * setLayerType(LAYER_TYPE_HARDWARE, paint);
     */
    @Multiline
    static String INFO_2;
}
