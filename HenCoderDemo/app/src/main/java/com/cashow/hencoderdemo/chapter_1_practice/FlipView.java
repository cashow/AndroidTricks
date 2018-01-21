package com.cashow.hencoderdemo.chapter_1_practice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.cashow.hencoderdemo.common.BaseView;
import com.cashow.hencoderdemo.common.BitmapUtils;
import com.cashow.hencoderdemo.common.CommonUtils;

public class FlipView extends BaseView {
    private Paint paint;
    private Camera camera;

    private int bitmapWidth;
    private int bitmapHeight;
    private int bitmapX;
    private int bitmapY;

    private int cameraRotateY1;
    private int cameraRotateY2;
    private int canvasRotateY;

    public FlipView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public FlipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        camera = new Camera();

        bitmapWidth = 400;
        bitmapHeight = 400;
        bitmapX = 100;
        bitmapY = 100;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtils.dp2px(context, 300), CommonUtils.dp2px(context, 300));
        setLayoutParams(params);

        logoBitmap = BitmapUtils.getResizedBitmap(logoBitmap, bitmapWidth, bitmapHeight);

        post(() -> startAnim());
    }

    private void startAnim() {
        cameraRotateY1 = 0;
        canvasRotateY = 0;
        cameraRotateY2 = 0;

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofInt(this, "cameraRotateY1", 0, -30);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofInt(this, "canvasRotateY", 0, -270);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofInt(this, "cameraRotateY2", 0, 30);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                postDelayed(() -> startAnim(), 1000);
            }
        });
        animatorSet.playSequentially(objectAnimator1, objectAnimator2, objectAnimator3);
        animatorSet.start();
    }

    public void setCameraRotateY1(int cameraRotateY1) {
        this.cameraRotateY1 = cameraRotateY1;
        invalidate();
    }

    public void setCanvasRotateY(int canvasRotateY) {
        this.canvasRotateY = canvasRotateY;
        invalidate();
    }

    public void setCameraRotateY2(int cameraRotateY2) {
        this.cameraRotateY2 = cameraRotateY2;
        invalidate();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        camera.save();
        camera.rotateY(cameraRotateY1);
        canvas.translate(bitmapX + bitmapWidth / 2, bitmapY + bitmapHeight / 2);
        canvas.rotate(canvasRotateY);
        canvas.clipRect(0, -bitmapHeight, bitmapWidth, bitmapHeight);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.rotate(-canvasRotateY);
        canvas.translate(-bitmapX - bitmapWidth / 2, -bitmapY - bitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, bitmapX, bitmapY, paint);
        canvas.restore();

        canvas.save();
        camera.save();
        camera.rotateY(cameraRotateY2);
        canvas.translate(bitmapX + bitmapWidth / 2, bitmapY + bitmapHeight / 2);
        canvas.rotate(canvasRotateY);
        canvas.clipRect(-bitmapWidth, -bitmapHeight, 0, bitmapHeight);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.rotate(-canvasRotateY);
        canvas.translate(-bitmapX - bitmapWidth / 2, -bitmapY - bitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, bitmapX, bitmapY, paint);
        canvas.restore();
    }
}
