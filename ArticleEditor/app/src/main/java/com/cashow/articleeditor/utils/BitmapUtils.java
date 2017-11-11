package com.cashow.articleeditor.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;

public class BitmapUtils {
    /**
     * 读取本地的图片文件，并压缩到 1600 x 1600 以内
     */
    public static Bitmap getCompressedBitmap(String path) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, op);
        op.inSampleSize = 1;
        while (op.outWidth > 1600 || op.outHeight > 1600){
            op.outWidth /= 2;
            op.outHeight /= 2;
            op.inSampleSize *= 2;
        }
        op.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(path, op);

        ExifInterface exif = null;
        String orientString = null;
        try {
            exif = new ExifInterface(path);
            orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
            rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, op.outWidth / 2, op.outHeight / 2);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, op.outWidth, op.outHeight, matrix, true);

        return bitmap;
    }

    /**
     * 将bitmap缩放到 newWidth x newHeight
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        // newWidth 和 newHeight不能小于0
        if (newWidth <= 0) {
            newWidth = 1;
        }
        if (newHeight <= 0) {
            newHeight = 1;
        }

        int width = bm.getWidth();
        int height = bm.getHeight();
        //计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    /**
     * 将bitmap缩放到宽度或者高度等于MAX_WIDTH
     */
    public static Bitmap resizeBitmapToMaxWidth(Bitmap bitmap) {
        Bitmap mBitmap = Bitmap.createBitmap(bitmap);
        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        double ratio = Math.min(Constants.MAX_WIDTH / bitmapWidth, Constants.MAX_WIDTH / bitmapHeight);
        int newWidth = (int) (bitmapWidth * ratio);
        int newHeight = (int) (bitmapHeight * ratio);
        mBitmap = BitmapUtils.getResizedBitmap(mBitmap, newWidth, newHeight);
        return mBitmap;
    }

    /**
     * 显示超大图片, 显示前自动进行缩放 200*200大小
     * @param uri
     * @param draweeView
     */
    public static void displayBigImage(Uri uri, SimpleDraweeView draweeView){
        int width = 200, height = 200;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(request)
                .build();
        draweeView.setController(controller);
    }
}
