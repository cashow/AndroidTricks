package com.cashow.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * 图片处理相关的Utils
 */
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
     * 读取本地的图片文件，并压缩到 800 x 800 以内
     */
    public static Bitmap getCompressedBitmap(String path) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, op);

        op.inPreferredConfig = Bitmap.Config.ARGB_8888;
        op.inDither = true;
        op.inPurgeable = true;

        int inSampleSize = 1;
        if (op.outHeight > 800 || op.outWidth > 800) {
            final int halfHeight = op.outHeight / 2;
            final int halfWidth = op.outWidth / 2;

            while ((halfHeight / inSampleSize) > 800 || (halfWidth / inSampleSize) > 800) {
                inSampleSize *= 2;
            }
        }

        op.inSampleSize = inSampleSize;
        op.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeFile(path, op);

        try {
            ExifInterface exif = new ExifInterface(path);
            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;


            int rotationAngle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                rotationAngle = 270;

            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, op.outWidth, op.outHeight, matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > 800 || height > 800) {
            double ratio = Math.min(800.0 / width, 800.0 / height);
            int newWidth = (int) (width * ratio);
            int newHeight = (int) (height * ratio);
            bitmap = BitmapUtils.getResizedBitmap(bitmap, newWidth, newHeight);
        }
        return bitmap;
    }

    /**
     * 将图片压缩到宽度和高度小于或等于edgeLength，并截取中间的正方形部分
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            // 压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            // 从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    /**
     * 将bitmap转换成byte[]
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                // F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }

    /**
     * 合并两个bitmap
     */
    public static Bitmap mergeBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }

        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        // int fgWidth = foreground.getWidth();
        // int fgHeight = foreground.getHeight();
        // create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        // draw bg into
        cv.drawBitmap(background, 0, 0, null);// 在 0，0坐标开始画入bg
        // draw fg into
        cv.drawBitmap(foreground, 0, 0, null);// 在 0，0坐标开始画入fg ，可以从任意位置画入
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newbmp;
    }

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

    /**
     * 将图片转换成正方形，多余地方用白色填充
     */
    public static Bitmap getBitmapWithWhiteEdge(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        // 如果长宽相等则直接返回
        if (width == height)
            return bm;

        // 新图片的宽度和高度
        int newLength = (width > height) ? width : height;
        Bitmap newb = Bitmap.createBitmap(newLength, newLength, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newb);
        canvas.drawColor(0xffffffff);// 白色背景
        canvas.drawBitmap(bm, (newLength - width) / 2, (newLength - height) / 2, null);
        return newb;
    }

    /**
     * 给bitmap加上圆角
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 给bitmap加上圆角，可指定哪个部分有圆角
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels, boolean squareTL, boolean squareTR, boolean squareBL, boolean squareBR) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);

        float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        //draw rectangles over the corners we want to be square
        if (squareTL) {
            canvas.drawRect(0, h / 2, w / 2, h, paint);
        }
        if (squareTR) {
            canvas.drawRect(w / 2, h / 2, w, h, paint);
        }
        if (squareBL) {
            canvas.drawRect(0, 0, w / 2, h / 2, paint);
        }
        if (squareBR) {
            canvas.drawRect(w / 2, 0, w, h / 2, paint);
        }

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return output;
    }

    /**
     * 将Drawable转换成Bitmap
     */
    private static Bitmap getBitmapFromDrawable(Activity mActivity, int imageId) {
        Drawable draw = mActivity.getResources().getDrawable(imageId);
        return ((BitmapDrawable) draw).getBitmap();
    }
}
