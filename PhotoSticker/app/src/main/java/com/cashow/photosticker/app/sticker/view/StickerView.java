package com.cashow.photosticker.app.sticker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cashow.photosticker.liuliu.R;
import com.cashow.photosticker.utils.Utils;

public class StickerView extends RelativeLayout {

    private Context context;

    private float centerX;
    private float centerY;

    private TouchPoint scalePoint;
    private TouchPoint rotatePoint;
    private TouchPoint movePoint;

    private ImageView image_texture;
    private ImageView image_close;
    private ImageView image_rotate;

    private StickerListener stickerListener;

    private final static int IMAGE_MIN_WIDTH = 100;
    private final static double EPS = 1e-8;

    public StickerView(Context context) {
        super(context);
        init(context);
    }

    public StickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_sticker, this, true);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(Utils.dp2px(context, 160), Utils.dp2px(context, 160));
        setLayoutParams(params);

        image_texture = (ImageView) findViewById(R.id.image_texture);
        image_close = (ImageView) findViewById(R.id.image_close);
        image_rotate = (ImageView) findViewById(R.id.image_rotate);

        image_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                stickerListener.onStickerViewRemoved();
            }
        });
        image_rotate.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        bringToFront();
                        invalidate();

                        stickerListener.onStickerViewSelected();

                        // pre : 上次点击的坐标
                        scalePoint = new TouchPoint();
                        scalePoint.preX = event.getRawX();
                        scalePoint.preY = -event.getRawY();

                        rotatePoint = new TouchPoint();
                        rotatePoint.preX = event.getRawX();
                        rotatePoint.preY = -event.getRawY();

                        centerX = getX() + getMeasuredWidth() / 2.0f;
                        centerY = -(getY() + getMeasuredHeight() / 2.0f);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // current : 这次点击的坐标
                        scalePoint.currentX = event.getRawX();
                        scalePoint.currentY = -event.getRawY();

                        scaleView();

                        rotatePoint.currentX = event.getRawX();
                        rotatePoint.currentY = -event.getRawY();

                        rotateView();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        image_texture.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        invalidate();

                        stickerListener.onStickerViewSelected();

                        movePoint = new TouchPoint();
                        movePoint.preX = event.getRawX();
                        movePoint.preY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = event.getRawX() - movePoint.preX;
                        float dy = event.getRawY() - movePoint.preY;

                        setX(getX() + dx);
                        setY(getY() + dy);

                        movePoint.preX = event.getRawX();
                        movePoint.preY = event.getRawY();
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void setImageResource(int imageId) {
        image_texture.setImageResource(imageId);
    }

    private void scaleView() {
        LayoutParams params = (LayoutParams) getLayoutParams();

        // 现在触摸的点到中心点的距离
        double newLength = Math.sqrt((scalePoint.currentX - centerX) * (scalePoint.currentX - centerX)
                + (scalePoint.currentY - centerY) * (scalePoint.currentY - centerY));
        // 之前触摸的点到中心点的距离
        double oldLength = Math.sqrt((scalePoint.preX - centerX) * (scalePoint.preX - centerX)
                + (scalePoint.preY - centerY) * (scalePoint.preY - centerY));
        // 缩放的比例
        double scaleRatio = newLength / oldLength;

        int oldWidth = params.width;
        int newWidth = (int) Math.round(params.width * scaleRatio);

        newWidth = Math.max(Utils.dp2px(context, IMAGE_MIN_WIDTH), newWidth);

        // 修改贴纸的宽度和高度后向左上移动diffWidth，确保贴纸的中心点不变
        int diffWidth = (newWidth - oldWidth) / 2;
        if (diffWidth == 0)
            return;

        newWidth = oldWidth + diffWidth * 2;

        params.width = newWidth;
        params.height = newWidth;

        params.setMargins(params.leftMargin - diffWidth, params.topMargin - diffWidth, 0, 0);
        setLayoutParams(params);

        scalePoint.preX = scalePoint.currentX;
        scalePoint.preY = scalePoint.currentY;
    }

    private void rotateView() {
        double pi = Math.acos(-1.0);

        float x1 = rotatePoint.preX - centerX;
        float y1 = rotatePoint.preY - centerY;

        float x2 = rotatePoint.currentX - centerX;
        float y2 = rotatePoint.currentY - centerY;

        double cos = (x1 * x2 + y1 * y2) / Math.sqrt(x1 * x1 + y1 * y1) / Math.sqrt(x2 * x2 + y2 * y2);
        cos = Math.min(cos, 1.0);
        cos = Math.max(cos, 0.0);

        float diffRotate = (float) Math.acos(cos);
        diffRotate = (float) (diffRotate * 180.0f / pi);

        float cross = x1 * y2 - x2 * y1;
        if (cross > 0) {
            diffRotate = -diffRotate;
        }
        if (Math.abs(diffRotate) < EPS)
            return;
        setRotation(getRotation() + diffRotate);

        rotatePoint.preX = rotatePoint.currentX;
        rotatePoint.preY = rotatePoint.currentY;
    }

    public void setStickerListener(StickerListener stickerListener) {
        this.stickerListener = stickerListener;
    }

    public interface StickerListener {
        void onStickerViewRemoved();
        void onStickerViewSelected();
    }

    class TouchPoint {
        float preX;
        float preY;
        float currentX;
        float currentY;
    }
}
