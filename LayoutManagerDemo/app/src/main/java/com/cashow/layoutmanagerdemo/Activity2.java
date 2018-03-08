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
import com.cashow.layoutmanagerdemo.layoutmanager.MyLayoutManager2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link MyLayoutManager2}:
 * 只实现了简单的 onLayoutChildren() 和 scrollVerticallyBy() 的竖向 LayoutManager，没有实现 view 的回收，可以竖向滑动
 * 页面加载后 adapter 会调用 2*50 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 50 个
 * 在滑动中获取到的 RecyclerView 的子 view 数也是 50 个
 */
public class Activity2 extends AppCompatActivity {
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

        setMyLayoutManager2();

        recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                MLog.d("onScrollChange : " + recyclerview.getChildCount());
            }
        });
    }

    private void setMyLayoutManager2() {
        recyclerview.setLayoutManager(new MyLayoutManager2());
        recyclerview.setAdapter(myAdapter);
    }
}
