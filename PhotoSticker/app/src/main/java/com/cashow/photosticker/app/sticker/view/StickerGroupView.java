package com.cashow.photosticker.app.sticker.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cashow.photosticker.liuliu.R;
import com.cashow.photosticker.utils.BitmapUtils;
import com.cashow.photosticker.utils.Utils;

import java.util.LinkedList;
import java.util.List;

public class StickerGroupView extends RelativeLayout {

    private Context context;

    /**
     * 目前显示的贴纸的 id 列表
     */
    public LinkedList<Integer> stikerIdList;
    /**
     * 下一个贴纸的id
     */
    private Integer nextStikerId;
    /**
     * 目前显示的贴纸id
     */
    private Integer currentShowStikerId;
    /**
     * 贴纸控件的宽度
     */
    private final static int LAYOUT_WIDTH = 80;
    /**
     * 贴纸合并后生成的 bitmap 的宽度
     */
    private static final double IMAGE_WIDTH = 640.0;

    public StickerGroupView(Context context) {
        super(context);
        init(context);
    }

    public StickerGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickerGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        stikerIdList = new LinkedList<>();
        nextStikerId = 0;
        currentShowStikerId = -1;
    }

    private View getStickerView(int imageId) {
        // 取消之前选中的贴纸
        unselectCurrentSticker();

        final StickerView stickerView = new StickerView(context);
        stickerView.setImageResource(imageId);

        stickerView.setStickerListener(new StickerView.StickerListener() {
            @Override
            public void onStickerViewRemoved() {
                Integer id = (Integer) stickerView.getTag();
                stikerIdList.remove(id);
                removeView(stickerView);
                currentShowStikerId = -1;
            }

            @Override
            public void onStickerViewSelected() {
                unselectCurrentSticker();

                currentShowStikerId = (Integer) stickerView.getTag();

                // 选中贴纸后将这个贴纸移到所有贴纸的顶部
                stickerView.bringToFront();
                stickerView.requestLayout();
                invalidate();

                // 将 stikerIdList 里当前贴纸的 id 移动到最后一个
                // 这样处理是为了在合并贴纸时，最后绘制这个贴纸
                stikerIdList.remove(currentShowStikerId);
                stikerIdList.addLast(currentShowStikerId);

                selectSticker(currentShowStikerId);
            }
        });

        currentShowStikerId = nextStikerId;
        stickerView.setTag(nextStikerId);
        stikerIdList.add(nextStikerId);
        nextStikerId++;

        // 新添加的贴纸默认在图片的中间
        int marginLeft = Utils.getScreenWidth(context) / 2 - Utils.dp2px(context, LAYOUT_WIDTH);
        int marginTop = Utils.getScreenWidth(context) / 2 - Utils.dp2px(context, LAYOUT_WIDTH) + Utils.dp2px(context, 45);

        stickerView.setX(marginLeft);
        stickerView.setY(marginTop);

        return stickerView;
    }

    /**
     * 取消贴纸的选中状态，隐藏贴纸的边框
     */
    public void unselectCurrentSticker() {
        if (currentShowStikerId == -1)
            return;
        RelativeLayout layout = (RelativeLayout) findViewWithTag(currentShowStikerId);

        ImageView image_square = (ImageView) layout.findViewById(R.id.image_square);
        image_square.setVisibility(View.INVISIBLE);

        ImageView image_close = (ImageView) layout.findViewById(R.id.image_close);
        image_close.setVisibility(View.INVISIBLE);
        image_close.setOnClickListener(null);

        ImageView image_rotate = (ImageView) layout.findViewById(R.id.image_rotate);
        image_rotate.setVisibility(View.INVISIBLE);

        currentShowStikerId = -1;
    }

    /**
     * 选中贴纸，显示指定贴纸的边框
     */
    private void selectSticker(int id) {
        final RelativeLayout layout = (RelativeLayout) findViewWithTag(id);
        ImageView image_square = (ImageView) layout.findViewById(R.id.image_square);
        image_square.setVisibility(View.VISIBLE);

        ImageView image_close = (ImageView) layout.findViewById(R.id.image_close);
        image_close.setVisibility(View.VISIBLE);
        image_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                currentShowStikerId = -1;
                Integer id = (Integer) layout.getTag();
                stikerIdList.remove(id);
                removeView(layout);
            }
        });

        ImageView image_rotate = (ImageView) layout.findViewById(R.id.image_rotate);
        image_rotate.setVisibility(View.VISIBLE);
    }

    /**
     * 将所有的贴纸合并成 Bitmap
     */
    public Bitmap getStikerBitmap() {
        Bitmap bitmap = Bitmap.createBitmap((int) IMAGE_WIDTH, (int) IMAGE_WIDTH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int i = 0; i < stikerIdList.size(); i++) {
            int tag = stikerIdList.get(i);
            View textureView = findViewWithTag(tag);
            drawSticker(canvas, textureView);
        }
        bitmap = BitmapUtils.getResizedBitmap(bitmap, (int) IMAGE_WIDTH, (int) IMAGE_WIDTH);
        return bitmap;
    }

    /**
     * 这个函数是把贴纸转换成bitmap，根据屏幕与canvas宽度的比例计算出贴纸的位置和大小并画在canvas里
     */
    private void drawSticker(Canvas canvas, View textureView) {
        // 屏幕宽度与canvas宽度的比例，用来转换屏幕的坐标和canvas的坐标
        double ratio = IMAGE_WIDTH / Utils.getScreenWidth(context);

        // 获取贴纸的bitmap，这个bitmap的大小是屏幕上显示的大小
        Bitmap bitmap = BitmapUtils.convertViewToBitmap(textureView);

        // bitmap在canvas里的大小
        int newWidth = (int) (bitmap.getWidth() * ratio);
        int newHeight = (int) (bitmap.getHeight() * ratio);

        // 缩放bitmap
        bitmap = BitmapUtils.getResizedBitmap(bitmap, newWidth, newHeight);

        // 贴纸与屏幕左边的距离
        double leftInScreen = textureView.getX();
        // 贴纸与屏幕顶部的距离，再减去actionbar的高度
        double topInScreen = textureView.getY() - Utils.dp2px(context, 45);

        // 贴纸与canvas左边的距离
        int leftInCanvas = (int) (leftInScreen * ratio);
        // 贴纸与canvas顶部的距离
        int topInCanvas = (int) (topInScreen * ratio);

        // canvas旋转时需要以bitmap中心点为旋转点，所以canvas的顶点需要移动到bitmap中心点
        leftInCanvas += (int) (newWidth / 2.0);
        topInCanvas += (int) (newHeight / 2.0);

        canvas.save();
        canvas.translate(leftInCanvas, topInCanvas);
        canvas.rotate(textureView.getRotation());
        canvas.drawBitmap(bitmap, (int) (-newWidth / 2.0), (int) (-newHeight / 2.0), null);
        canvas.restore();
    }

    public void addStikerView(int imageId) {
        addView(getStickerView(imageId));
    }
}
