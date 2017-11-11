package com.cashow.articleeditor.app.pickphoto;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.cashow.articleeditor.R;
import com.cashow.articleeditor.utils.BaseActivity;
import com.cashow.articleeditor.utils.BitmapUtils;
import com.cashow.articleeditor.utils.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PickPhotoActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private GridView allGridview;

    private AllGridAdapter allGridAdapter;

    private HashMap<String, List<String>> mGruopMap = new HashMap<>();
    private List<String> allPhotoArray = new ArrayList<>();

    private AbsListView.OnScrollListener onScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        initView();

        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            rxGetGalleryPhotos();
        } else {
            EasyPermissions.requestPermissions(this, "需要获取读取相册的权限", 1, perms);
        }
    }

    private void initView() {
        allGridview = (GridView) findViewById(R.id.all_gridview);

        allGridview.setOnScrollListener(onScrollListener);

        allGridAdapter = new AllGridAdapter();
        allGridview.setAdapter(allGridAdapter);

        onScrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        Fresco.getImagePipeline().resume();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        Fresco.getImagePipeline().pause();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        Fresco.getImagePipeline().pause();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        };
    }

    private void rxGetGalleryPhotos() {
        Subscription subscription = Observable.create(subscriber -> {
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = context.getContentResolver();

            // 只查询jpeg和png的图片
            Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"},
                    MediaStore.Images.Media.DATE_MODIFIED);

            while (mCursor.moveToNext()) {
                try {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (TextUtils.isEmpty(path)) {
                        continue;
                    }
                    // 获取该图片的父路径名
                    String parentName = new File(path).getParentFile().getName();

                    // 根据父路径名将图片放入到mGruopMap中
                    if (!mGruopMap.containsKey(parentName)) {
                        List<String> chileList = new ArrayList<String>();
                        chileList.add(path);
                        mGruopMap.put(parentName, chileList);
                    } else {
                        mGruopMap.get(parentName).add(0, path);
                    }
                    allPhotoArray.add(path);
                } catch (Exception e) {
                }
            }
            Collections.reverse(allPhotoArray);
            mCursor.close();
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        allGridAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Object t) {
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        rxGetGalleryPhotos();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    private class AllGridAdapter extends BaseAdapter {

        public class ViewHolder {
            SimpleDraweeView imagePhoto;
        }

        @Override
        public int getCount() {
            return allPhotoArray.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final AllGridAdapter.ViewHolder holder;
            View view = convertView;
            if (convertView == null) {
                view = mActivity.getLayoutInflater().inflate(R.layout.gridview_photo, parent, false);
                holder = new AllGridAdapter.ViewHolder();
                assert view != null;
                holder.imagePhoto = (SimpleDraweeView) view.findViewById(R.id.image_photo);
                view.setTag(holder);
            } else {
                holder = (AllGridAdapter.ViewHolder) view.getTag();
            }

            holder.imagePhoto.setOnClickListener(v -> resizePhoto(allPhotoArray.get(position)));
            BitmapUtils.displayBigImage(Uri.parse("file://" + allPhotoArray.get(position)),holder.imagePhoto);
            return view;
        }
    }

    private void resizePhoto(String path) {
        showMyDialog(true);
        Subscription subscription = Observable.just(path)
                .observeOn(Schedulers.io())
                .map(imagePath -> BitmapUtils.getCompressedBitmap(imagePath))
                .map(bitmap -> BitmapUtils.resizeBitmapToMaxWidth(bitmap))
                .map(bitmap -> saveBitmap(bitmap))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(savePath -> {
                    hideMyDialog();
                    setPickPhotoResult(savePath);
                });
        addSubscription(subscription);
    }

    private String saveBitmap(Bitmap bitmap) {
        String savePath = Utils.getArticlePath(context);
        Utils.savePNG(bitmap, savePath);
        return savePath;
    }

    private void setPickPhotoResult(String path) {
        hideMyDialog();
        Intent intent = new Intent();
        intent.putExtra("photoPath", path);
        mActivity.setResult(Activity.RESULT_OK, intent);
        mActivity.finish();
    }
}