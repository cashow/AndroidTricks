package com.cashow.photobrush.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

public class BitmapUtils {

    /**
     * 将bitmap各个像素的rgb值改成参数里的r,g,b
     */
    public static Bitmap convertImg(Bitmap img, int r, int g, int b) {
        int width = img.getWidth(); // 获取位图的宽
        int height = img.getHeight(); // 获取位图的高

        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = pixels[width * i + j];
                int alpha = Color.alpha(pixel);

                pixel = Color.argb(alpha, r, g, b);
                pixels[width * i + j] = pixel;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
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
     * 旋转bitmap
     */
    public static Bitmap adjustPhotoRotation(Bitmap bm, float orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }
}
