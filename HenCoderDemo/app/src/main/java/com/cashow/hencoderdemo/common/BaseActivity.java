package com.cashow.hencoderdemo.common;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cashow.hencoderdemo.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.layout_container)
    protected ViewGroup layoutContainer;
    @BindView(R.id.scrollView)
    protected ScrollView scrollView;

    protected Context context;
    private Constructor constructor;
    protected Class[] baseViewClasses = getBaseViewClasses();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        context = getApplicationContext();

        ButterKnife.bind(this);

        if (baseViewClasses.length > 0) {
            addDemoView(baseViewClasses[baseViewClasses.length - 1]);
        }
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            setTitle(getClass().getSimpleName());
        } else {
            setTitle(title);
        }
    }

    private void addDemoView(Class<? extends BaseView> viewClass) {
        try {
            // 找到 BaseView 的构造函数
            constructor = viewClass.getConstructor(Context.class, Integer.class);
            // 生成一个 BaseView
            BaseView baseView = (BaseView) constructor.newInstance(context, 0);
            // 拿到 BaseView 里的 viewTypeCount
            int viewTypeCount = baseView.getViewTypeCount();

            layoutContainer.removeAllViews();
            for (int i = 0; i < viewTypeCount; i++) {
                ViewGroup infoContainerView = getInfoContainerView();
                BaseView baseView1 = getBaseView(i);
                String info = getInfo(baseView1, i);
                infoContainerView.addView(getInfoView(info, true));
                infoContainerView.addView(baseView1);
                layoutContainer.addView(infoContainerView);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("get view failed");
        }
    }

    protected void addMyDemoView(View view, String info) {
        ViewGroup infoContainerView = getInfoContainerView();
        int padding = CommonUtils.dp2px(context, 10);
        infoContainerView.setPadding(padding, padding, padding, padding);
        infoContainerView.addView(getInfoView(info, false));
        infoContainerView.addView(getDividerView(padding));
        infoContainerView.addView(view);
        layoutContainer.addView(infoContainerView);
    }

    private ViewGroup getInfoContainerView() {
        LinearLayout view = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        int margin = CommonUtils.dp2px(context, 5);
        layoutParams.setMargins(2 * margin, margin, 2 * margin, margin);
        view.setBackgroundColor(getResources().getColor(android.R.color.white));
        view.setElevation(5);
        view.setOrientation(LinearLayout.VERTICAL);
        return view;
    }

    /**
     * 给 layoutContainer 添加一个 40 像素高度的 divider
     */
    protected void addDivider() {
        View view = getDividerView(40);
        layoutContainer.addView(view);
    }

    /**
     * 获取一个高度为 height 的 view
     */
    private View getDividerView(int height) {
        View view = new View(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        view.setLayoutParams(layoutParams);
        return view;
    }

    /**
     * 生成一个 BaseView
     */
    private BaseView getBaseView(int viewType) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        BaseView baseView = (BaseView) constructor.newInstance(context, viewType);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtils.dp2px(context, 100), CommonUtils.dp2px(context, 100));
        int margin = CommonUtils.dp2px(context, 10);
        params.setMargins(margin, margin, margin, margin);
        baseView.setLayoutParams(params);
        return baseView;
    }

    /**
     * 获取 BaseView 的注释
     */
    private String getInfo(BaseView baseView, int viewType) {
        try {
            Class clz = baseView.getClass();
            Field field = clz.getDeclaredField("INFO_" + viewType);
            field.setAccessible(true);
            return field.get(baseView).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private View getInfoView(String info, boolean isSetMargin) {
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int marginTop = CommonUtils.dp2px(context, 10);
        int marginBottom = 0;
        int marginLeft = CommonUtils.dp2px(context, 10);
        int marginRight = CommonUtils.dp2px(context, 10);
        if (isSetMargin) {
            params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        }
        textView.setLayoutParams(params);
        textView.setTextColor(Color.parseColor("#5b4b47"));
        textView.setLineSpacing(5, 1.0f);
        textView.setText(info);
        return textView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (baseViewClasses.length > 0) {
            addDemoView(baseViewClasses[item.getItemId()]);
            setTitle(item.getTitle());
            scrollView.scrollTo(0, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (baseViewClasses.length > 0) {
            for (int i = 0; i < baseViewClasses.length; i++) {
                menu.add(Menu.NONE, i, Menu.NONE, baseViewClasses[i].getSimpleName());
            }
        }
        return true;
    }

    protected abstract Class[] getBaseViewClasses();
}
