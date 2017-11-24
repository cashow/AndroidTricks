package com.cashow.photosticker.app.sticker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cashow.photosticker.app.sticker.view.SquareImageView;
import com.cashow.photosticker.app.sticker.view.StickerGroupView;
import com.cashow.photosticker.app.zoomphoto.ZoomPhotoActivity;
import com.cashow.photosticker.liuliu.R;
import com.cashow.photosticker.utils.BaseActivity;
import com.cashow.photosticker.utils.BitmapUtils;
import com.cashow.photosticker.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StickerActivity extends BaseActivity {

    @BindView(R.id.image_photo)
    SquareImageView imagePhoto;
    @BindView(R.id.sticker_view)
    StickerGroupView stickerView;
    @BindView(R.id.image_sticker_1)
    View imageSticker1;
    @BindView(R.id.image_sticker_2)
    View imageSticker2;
    @BindView(R.id.image_sticker_3)
    View imageSticker3;

    /**
     * 原图
     */
    private Bitmap oriBitmap;

    /**
     * 要加载的图片最终的宽度
     */
    private static final int IMAGE_WIDTH = 640;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

        initView();

        loadOriBitmap();
    }

    private void initView() {
        mActivity.setFullScreen();

        ButterKnife.bind(this);

        imagePhoto.setOnClickListener(v -> stickerView.unselectCurrentSticker());
    }

    /**
     * 加载要处理的图片
     */
    private void loadOriBitmap() {
        Drawable drawable = getResources().getDrawable(R.drawable.demo);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        oriBitmap = BitmapUtils.getResizedBitmap(bitmap, IMAGE_WIDTH, IMAGE_WIDTH);
        imagePhoto.setImageBitmap(oriBitmap);
    }

    @OnClick(R.id.image_sticker_1)
    void onSticker1click() {
        stickerView.addStikerView(R.drawable.sticker_1);
    }

    @OnClick(R.id.image_sticker_2)
    void onSticker2click() {
        stickerView.addStikerView(R.drawable.sticker_2);
    }

    @OnClick(R.id.image_sticker_3)
    void onSticker3click() {
        stickerView.addStikerView(R.drawable.sticker_3);
    }

    @OnClick(R.id.text_confirm)
    void onConfirmClick() {
        stickerView.unselectCurrentSticker();
        // 生成最终的图片
        showMyDialog(false);
        Subscription subscription = Observable.just(getSaveImagePath())
                .observeOn(Schedulers.io())
                .map(imagePath -> {
                    Bitmap layoutBitmap = getProcessedBitmap();
                    if (layoutBitmap == null) {
                        return false;
                    }
                    Utils.savePNG(layoutBitmap, imagePath);
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSaveBitmap -> {
                    hideMyDialog();
                    if (!isSaveBitmap) {
                        Toast.makeText(context, R.string.process_photo_failed, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    startUploadPhotoDetailActivity(mActivity);
                });
        addSubscription(subscription);
    }

    private String getSaveImagePath() {
        return Utils.getImagePath(context);
    }

    public static void startUploadPhotoDetailActivity(Activity mActivity) {
        Intent intent = new Intent();
        intent.setClass(mActivity, ZoomPhotoActivity.class);
        mActivity.startActivity(intent);
    }

    /**
     * 获取最终要生成的图片
     */
    private Bitmap getProcessedBitmap() {
        // 最终要生成的图片
        Bitmap processedBitmap = Bitmap.createBitmap(oriBitmap.getWidth(), oriBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        int width = processedBitmap.getWidth();
        Canvas canvas = new Canvas(processedBitmap);

        // 画原图
        canvas.drawBitmap(oriBitmap, 0, 0, null);

        // 合成贴纸效果
        mergeBitmap(canvas, width, stickerView.getStikerBitmap());
        return processedBitmap;
    }

    private void mergeBitmap(Canvas canvas, int width, Bitmap newBitmap) {
        newBitmap = BitmapUtils.getResizedBitmap(newBitmap, width, width);
        canvas.drawBitmap(newBitmap, 0, 0, null);
    }
}