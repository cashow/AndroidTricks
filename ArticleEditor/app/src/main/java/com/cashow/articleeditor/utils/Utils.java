package com.cashow.articleeditor.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

public class Utils {
    // 将dp转换成px值
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static String getArticlePath(Context context) {
        String fileName = "article_" + System.currentTimeMillis() + ".png";
        String savePath = context.getFilesDir().getPath();
        File folder = new File(savePath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File stikerFolder = new File(savePath + File.separator + "article");
        if (!stikerFolder.exists()) {
            stikerFolder.mkdir();
        }

        return savePath + File.separator + "article" + File.separator + fileName;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static void savePNG(Bitmap bm, String path) {
        try {
            FileOutputStream fout = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
