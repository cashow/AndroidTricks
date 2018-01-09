package com.cashow.hencoderdemo.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cashow.hencoderdemo.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.layout_container)
    ViewGroup layoutContainer;

    protected Context context;
    private Constructor constructor;
    protected Class[] baseViewClasses = getBaseViewClasses();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        context = getApplicationContext();

        ButterKnife.bind(this);

        addDemoView(baseViewClasses[baseViewClasses.length - 1]);
    }

    protected void addDemoView(Class<? extends BaseView> viewClass) {
        try {
            constructor = viewClass.getConstructor(Context.class, Integer.class);
            BaseView baseView = (BaseView) constructor.newInstance(context, 0);
            int viewTypeCount = baseView.getViewTypeCount();

            layoutContainer.removeAllViews();
            for (int i = 0; i < viewTypeCount; i++) {
                layoutContainer.addView(getBaseView(i));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("get view failed");
        }
    }

    private BaseView getBaseView(int viewType) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        BaseView baseView = (BaseView) constructor.newInstance(context, viewType);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtils.dp2px(context, 100), CommonUtils.dp2px(context, 100));
        int margin = CommonUtils.dp2px(context, 10);
        params.setMargins(margin, margin, margin, margin);
        baseView.setLayoutParams(params);
        return baseView;
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

    protected abstract Class[] getBaseViewClasses();
}
