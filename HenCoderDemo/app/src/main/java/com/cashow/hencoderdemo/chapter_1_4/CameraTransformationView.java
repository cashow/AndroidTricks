package com.cashow.hencoderdemo.chapter_1_4;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;
import com.cashow.hencoderdemo.common.BitmapUtils;

public class CameraTransformationView extends BaseView {
    private Paint paint;
    private Camera camera;

    public CameraTransformationView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public CameraTransformationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraTransformationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        camera = new Camera();
        logoBitmap = BitmapUtils.getResizedBitmap(logoBitmap, 150, 150);
    }

    @Override
    public int getViewTypeCount() {
        return 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * Camera 一共有四个 rotate 方法：
         * rotateX(deg) rotateY(deg) rotateZ(deg) rotate(x, y, z)
         *
         * Camera 和 Canvas 一样也需要保存和恢复状态才能正常绘制，不然在界面刷新之后绘制就会出现问题。
         *
         * 如果你需要图形左右对称，需要配合上 Canvas.translate()，在三维旋转之前把绘制内容的中心点移动到原点，即旋转的轴心，然后在三维旋转后再把投影移动回来
         * Canvas 的几何变换顺序是反的，所以要把移动到中心的代码写在下面，把从中心移动回来的代码写在上面。
         *
         * 移动 Camera
         * Camera.translate(float x, float y, float z)
         *
         * 设置虚拟相机的位置
         * Camera.setLocation(x, y, z)
         * 注意！这个方法有点奇葩，它的参数的单位不是像素，而是 inch，英寸。
         * 这种设计源自 Android 底层的图像引擎 Skia 。
         * 在 Skia 中，Camera 的位置单位是英寸，英寸和像素的换算单位在 Skia 中被写死为了 72 像素，而 Android 中把这个换算单位照搬了过来。
         * 在 Camera 中，相机的默认位置是 (0, 0, -8)（英寸）。8 x 72 = 576，所以它的默认位置是 (0, 0, -576)（像素）。
         * 如果绘制的内容过大，当它翻转起来的时候，就有可能出现图像投影过大的「糊脸」效果。而且由于换算单位被写死成了 72 像素，而不是和设备 dpi 相关的，所以在像素越大的手机上，这种「糊脸」效果会越明显。
         * 而使用 setLocation() 方法来把相机往后移动，就可以修复这种问题。
         */
        canvas.save();
        switch (viewType) {
            case 0:
                canvas.drawBitmap(logoBitmap, 50, 100, paint);
                break;
            case 1:
                camera.save();
                camera.rotateX(30); // 旋转 Camera 的三维空间
                camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
                camera.restore();

                canvas.drawBitmap(logoBitmap, 50, 100, paint);
                break;
            case 2:
                camera.save();
                camera.rotateY(30);
                camera.applyToCanvas(canvas);
                camera.restore();

                canvas.drawBitmap(logoBitmap, 50, 100, paint);
                break;
            case 3:
                camera.save();
                camera.rotateZ(30);
                camera.applyToCanvas(canvas);
                camera.restore();

                canvas.drawBitmap(logoBitmap, 50, 100, paint);
                break;
            case 4:
                camera.save();
                camera.rotateX(30); // 旋转 Camera 的三维空间
                canvas.translate(50 + logoBitmap.getWidth() / 2, 100 + logoBitmap.getHeight() / 2);
                camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
                canvas.translate(-50 - logoBitmap.getWidth() / 2, -100 - logoBitmap.getHeight() / 2);
                camera.restore();

                canvas.drawBitmap(logoBitmap, 50, 100, paint);
                break;
            case 5:
                camera.save();
                camera.rotateY(30);
                canvas.translate(50 + logoBitmap.getWidth() / 2, 100 + logoBitmap.getHeight() / 2);
                camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
                canvas.translate(-50 - logoBitmap.getWidth() / 2, -100 - logoBitmap.getHeight() / 2);
                camera.restore();

                canvas.drawBitmap(logoBitmap, 50, 100, paint);
                break;
            case 6:
                camera.save();
                camera.rotateZ(30);
                canvas.translate(50 + logoBitmap.getWidth() / 2, 100 + logoBitmap.getHeight() / 2);
                camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
                canvas.translate(-50 - logoBitmap.getWidth() / 2, -100 - logoBitmap.getHeight() / 2);
                camera.restore();

                canvas.drawBitmap(logoBitmap, 50, 100, paint);
                break;
            case 7:
                camera.save();
                camera.setLocation(0, 0, -8);
                camera.rotateX(30); // 旋转 Camera 的三维空间
                canvas.translate(50 + logoBitmap.getWidth() / 2, 100 + logoBitmap.getHeight() / 2);
                camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
                canvas.translate(-50 - logoBitmap.getWidth() / 2, -100 - logoBitmap.getHeight() / 2);
                camera.restore();

                canvas.drawBitmap(logoBitmap, 50, 100, paint);
                break;
            case 8:
                camera.save();
                camera.setLocation(0, 0, -2);
                camera.rotateX(30); // 旋转 Camera 的三维空间
                canvas.translate(50 + logoBitmap.getWidth() / 2, 100 + logoBitmap.getHeight() / 2);
                camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
                canvas.translate(-50 - logoBitmap.getWidth() / 2, -100 - logoBitmap.getHeight() / 2);
                camera.restore();

                canvas.drawBitmap(logoBitmap, 50, 100, paint);
                break;
            case 9:
                camera.save();
                camera.setLocation(0, 0, -16);
                camera.rotateX(30); // 旋转 Camera 的三维空间
                canvas.translate(50 + logoBitmap.getWidth() / 2, 100 + logoBitmap.getHeight() / 2);
                camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
                canvas.translate(-50 - logoBitmap.getWidth() / 2, -100 - logoBitmap.getHeight() / 2);
                camera.restore();

                canvas.drawBitmap(logoBitmap, 50, 100, paint);
                break;
        }
        canvas.restore();
    }
}
