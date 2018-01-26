package com.cashow.daggerdemo.common.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class TestObject4 {
    public List<Bitmap> bitmapList;

    public TestObject4() {
        bitmapList = new ArrayList<>();
        bitmapList.add(getBitmap());
        bitmapList.add(getBitmap());
        bitmapList.add(getBitmap());
        bitmapList.add(getBitmap());
        bitmapList.add(getBitmap());
        bitmapList.add(getBitmap());
    }

    private Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(3000, 3000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#000000"));
        return bitmap;
    }

    public List<Bitmap> getBitmapList() {
        return bitmapList;
    }
}
