package com.cashow.layoutmanagerdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cashow.layoutmanagerdemo.adapter.MyAdapter;
import com.cashow.layoutmanagerdemo.adapter.MyCardAdapter;
import com.cashow.layoutmanagerdemo.adapter.MyFlowAdapter;
import com.cashow.layoutmanagerdemo.adapter.MyImageAdapter;
import com.cashow.layoutmanagerdemo.layoutmanager.MyFlowLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link MyFlowLayoutManager}:
 * 流式布局，修改自 {@link com.cashow.layoutmanagerdemo.layoutmanager.FlowLayoutManager}
 * 页面加载后 adapter 会调用 13 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 13 个
 * 在滑动中获取到的 RecyclerView 的子 view 数有 13、14 个
 */
public class Activity5 extends AppCompatActivity {
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

        setMyFlowLayoutManager();

        recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                MLog.d("onScrollChange : " + recyclerview.getChildCount());
            }
        });
    }

    private void setMyFlowLayoutManager() {
        recyclerview.setLayoutManager(new MyFlowLayoutManager());
        recyclerview.setAdapter(myFlowAdapter);
    }
}
