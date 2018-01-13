package com.cashow.hencoderdemo.common;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashow.hencoderdemo.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.layout_container)
    protected ViewGroup layoutContainer;

    protected Context context;
    private Constructor constructor;
    protected Class[] baseViewClasses = getBaseViewClasses();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        context = getApplicationContext();

        ButterKnife.bind(this);

        if (isAutoAddDemoView()) {
            addDemoView(baseViewClasses[baseViewClasses.length - 1]);
        }
    }

    protected boolean isAutoAddDemoView() {
        return true;
    }

    private void addDemoView(Class<? extends BaseView> viewClass) {
        try {
            constructor = viewClass.getConstructor(Context.class, Integer.class);
            BaseView baseView = (BaseView) constructor.newInstance(context, 0);
            int viewTypeCount = baseView.getViewTypeCount();

            layoutContainer.removeAllViews();
            for (int i = 0; i < viewTypeCount; i++) {
                BaseView baseView1 = getBaseView(i);
                layoutContainer.addView(getInfoView(baseView1.getViewTypeInfo(i)));
                layoutContainer.addView(baseView1);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("get view failed");
        }
    }

    protected void addView(View view) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200);
        view.setLayoutParams(layoutParams);
        layoutContainer.addView(view);

        addDivider();
    }

    protected void addDivider() {
        View view = new View(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40);
        view.setLayoutParams(layoutParams);
        layoutContainer.addView(view);
    }

    private BaseView getBaseView(int viewType) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        BaseView baseView = (BaseView) constructor.newInstance(context, viewType);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtils.dp2px(context, 100), CommonUtils.dp2px(context, 100));
        int margin = CommonUtils.dp2px(context, 10);
        params.setMargins(margin, margin, margin, margin);
        baseView.setLayoutParams(params);
        return baseView;
    }

    private View getInfoView(String info) {
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int marginTop = CommonUtils.dp2px(context, 20);
        int marginBottom = CommonUtils.dp2px(context, 5);
        int marginLeft = CommonUtils.dp2px(context, 20);
        int marginRight = CommonUtils.dp2px(context, 20);
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        textView.setMinWidth(CommonUtils.dp2px(context, 100));
        textView.setLayoutParams(params);
        textView.setTextColor(Color.parseColor("#5b4b47"));
        textView.setText(info);
        return textView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isAutoAddDemoView()) {
            addDemoView(baseViewClasses[item.getItemId()]);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAutoAddDemoView()) {
            for (int i = 0; i < baseViewClasses.length; i++) {
                menu.add(Menu.NONE, i, Menu.NONE, baseViewClasses[i].getSimpleName());
            }
        }
        return true;
    }

    protected abstract Class[] getBaseViewClasses();
}
