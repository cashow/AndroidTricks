package com.cashow.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件处理相关的Utils
 */
public class FileUtils {
    /**
     * 复制文件
     */
    public static int copyFile(String fromFile, String toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 根据Uri获取文件的绝对位置
     */
    public static String getFilePathByContentResolver(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        String filePath = null;
        if (null == c) {
            throw new IllegalArgumentException("Query on " + uri + " returns null result.");
        }
        try {
            if ((c.getCount() != 1) || !c.moveToFirst()) {

            } else {
                filePath = c.getString(c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            }
        } finally {
            c.close();
        }
        return filePath;
    }

    /**
     * 将bitmap保存成png文件
     */
    public static boolean savePNG(Bitmap bm, String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fout = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将bitmap保存成png文件并在Exif里指定图片的旋转角度
     */
    public static void savePNG(Bitmap bm, String path, int orientation) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fout = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();

            ExifInterface exifi = new ExifInterface(path);
            exifi.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(orientation));
            exifi.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
