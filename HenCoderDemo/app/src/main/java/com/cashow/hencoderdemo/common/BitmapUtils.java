package com.cashow.hencoderdemo.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


/**
 * 图片处理相关的Utils
 */
public class BitmapUtils {
    /**
     * 将bitmap缩放到 newWidth x newHeight
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        // newWidth 和 newHeight不能小于0
        if (newWidth <= 0) {
            newWidth = 1;
        }
        if (newHeight <= 0){
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
     * 将 drawableId 转成 bitmap
     */
    public static Bitmap drawableToBitmap(Context context, int drawableId) {
        Drawable drawable = context.getDrawable(drawableId);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }
}
