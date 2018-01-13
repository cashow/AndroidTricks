package com.cashow.hencoderdemo.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cashow.hencoderdemo.R;

public abstract class BaseView extends View {
    protected Context context;
    protected Bitmap logoBitmap;
    protected Bitmap testBitmap;
    protected int viewType;
    protected int logoWidth;
    protected int logoHeight;

    public BaseView(Context context, Integer viewType) {
        super(context);
        this.viewType = viewType;
        init(context, null);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @CallSuper
    protected void init(Context context, AttributeSet attrs) {
        this.context = context;
        logoBitmap = BitmapUtils.drawableToBitmap(context, R.drawable.ic_launcher);
        testBitmap = BitmapUtils.drawableToBitmap(context, R.drawable.test2);
        logoWidth = logoBitmap.getWidth();
        logoHeight = logoBitmap.getHeight();
    }

    public abstract int getViewTypeCount();

    public String getViewTypeInfo(int viewType) {
        return "";
    }
}
