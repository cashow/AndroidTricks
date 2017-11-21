package com.cashow.photobrush.app.zoomphoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.cashow.photobrush.R;
import com.cashow.photobrush.utils.BaseActivity;
import com.cashow.photobrush.utils.Utils;
import com.cashow.photobrush.app.zoomphoto.view.ZoomImageView;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ZoomPhotoActivity extends BaseActivity {
    private ZoomImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_photo);

        setFullScreen();

        photo = (ZoomImageView) findViewById(R.id.image_photo);

        photo.setOnPhotoTapListener((view, x, y) -> finish());

        Bitmap loadBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.load_pic);
        photo.setImageBitmap(loadBitmap);
        rxDiaplayImage();
    }

    private void rxDiaplayImage(){
        Subscription subscription = Observable.just("")
            .observeOn(Schedulers.io())
            .map(s -> BitmapFactory.decodeFile(Utils.getImagePath(context)))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(bmp -> {
                if (bmp != null) {
                    photo.setImageBitmap(bmp);
                }
            });
        addSubscription(subscription);
    }
}