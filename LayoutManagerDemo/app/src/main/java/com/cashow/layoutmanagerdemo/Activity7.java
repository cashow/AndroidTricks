package com.cashow.layoutmanagerdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import com.cashow.layoutmanagerdemo.adapter.MyAdapter;
import com.cashow.layoutmanagerdemo.adapter.MyCardAdapter;
import com.cashow.layoutmanagerdemo.adapter.MyFlowAdapter;
import com.cashow.layoutmanagerdemo.adapter.MyImageAdapter;
import com.cashow.layoutmanagerdemo.layoutmanager.MyAnimHorizontalLayoutManager;
import com.cashow.layoutmanagerdemo.layoutmanager.MyHorizontalLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link MyAnimHorizontalLayoutManager}:
 * 带有滑动效果的横向的 LayoutManager，修改自 {@link MyHorizontalLayoutManager}
 * 页面加载后 adapter 会调用 3 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 2 个
 * 在滑动中获取到的 RecyclerView 的子 view 数有 3 个
 */
public class Activity7 extends AppCompatActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    MyAdapter myAdapter;
    MyFlowAdapter myFlowAdapter;
    MyImageAdapter myImageAdapter;
    MyCardAdapter myCardAdapter;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        ButterKnife.bind(this);

        myAdapter = new MyAdapter();
        myFlowAdapter = new MyFlowAdapter();
        myImageAdapter = new MyImageAdapter();
        myCardAdapter = new MyCardAdapter();

        setMyAnimHorizontalLayoutManager();

        recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                MLog.d("onScrollChange : " + recyclerview.getChildCount());
            }
        });
    }

    private void setMyAnimHorizontalLayoutManager() {
        recyclerview.setLayoutManager(new MyAnimHorizontalLayoutManager());
        recyclerview.setAdapter(myImageAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerview);
    }
}
