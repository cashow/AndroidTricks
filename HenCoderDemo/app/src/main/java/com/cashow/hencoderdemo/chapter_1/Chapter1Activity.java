package com.cashow.hencoderdemo.chapter_1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.cashow.hencoderdemo.common.BaseActivity;

public class Chapter1Activity extends BaseActivity {
    private Class[] baseViewClasses = new Class[]{DrawColorView.class,
            DrawCircleView.class, DrawRectView.class, DrawPointView.class, DrawOvalView.class,
            DrawLineView.class, DrawRoundRectView.class, DrawArcView.class, DrawPathView.class,
            DrawBitmapView.class, DrawTextView.class};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addDemoView(baseViewClasses[baseViewClasses.length - 1]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        addDemoView(baseViewClasses[item.getItemId()]);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        for (int i = 0; i < baseViewClasses.length; i++) {
            menu.add(Menu.NONE, i, Menu.NONE, baseViewClasses[i].getSimpleName());
        }
        return true;
    }
}
