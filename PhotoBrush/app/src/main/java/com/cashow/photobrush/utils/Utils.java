package com.cashow.photobrush.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    // 将dp转换成px值
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

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

    public static void deleteBrushFile(Context context) {
        String savePath = context.getFilesDir().getPath();
        File folder = new File(savePath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File stikerFolder = new File(savePath + File.separator + "brush");
        if (stikerFolder.exists()) {
            stikerFolder.delete();
        }
    }

    public static String getBrushPath(Context context, int id) {
        String fileName = id + ".png";
        String savePath = context.getFilesDir().getPath();
        File folder = new File(savePath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File stikerFolder = new File(savePath + File.separator + "brush");
        if (!stikerFolder.exists()) {
            stikerFolder.mkdir();
        }

        return savePath + File.separator + "brush" + File.separator + fileName;
    }

    public static String getImagePath(Context context) {
        String savePath = context.getFilesDir().getPath();
        File folder = new File(savePath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return savePath + File.separator + "liuliu_temp.png";
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static boolean isInView(View view, float rawX, float rawY) {
        int scrcoords[] = new int[2];
        view.getLocationOnScreen(scrcoords);
        float x = rawX + view.getLeft() - scrcoords[0];
        float y = rawY + view.getTop() - scrcoords[1];
        if (x >= view.getLeft() && x <= view.getRight() && y >= view.getTop() && y <= view.getBottom())
            return true;
        return false;
    }
}
