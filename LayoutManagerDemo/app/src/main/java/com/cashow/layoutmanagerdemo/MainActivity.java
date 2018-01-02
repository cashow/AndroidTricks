package com.cashow.layoutmanagerdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import com.cashow.layoutmanagerdemo.layoutmanager.MyAnimHorizontalLayoutManager;
import com.cashow.layoutmanagerdemo.layoutmanager.MyAnimHorizontalLayoutManager2;
import com.cashow.layoutmanagerdemo.layoutmanager.MyFlowLayoutManager;
import com.cashow.layoutmanagerdemo.layoutmanager.MyGalleryLayoutManager;
import com.cashow.layoutmanagerdemo.layoutmanager.MyHorizontalLayoutManager;
import com.cashow.layoutmanagerdemo.layoutmanager.MyLayoutManager;
import com.cashow.layoutmanagerdemo.layoutmanager.MyLayoutManager2;
import com.cashow.layoutmanagerdemo.layoutmanager.MyLayoutManager3;
import com.cashow.layoutmanagerdemo.layoutmanager.MyLinearLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 每个 LayoutManager 的区别：
 *
 * {@link MyLayoutManager}:
 * 只实现了简单的 onLayoutChildren() 的竖向 LayoutManager，没有实现 view 的回收，不可滑动
 * 页面加载后 adapter 会调用 2*50 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 50 个
 *
 * {@link MyLayoutManager2}:
 * 只实现了简单的 onLayoutChildren() 和 scrollVerticallyBy() 的竖向 LayoutManager，没有实现 view 的回收，可以竖向滑动
 * 页面加载后 adapter 会调用 2*50 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 50 个
 * 在滑动中获取到的 RecyclerView 的子 view 数也是 50 个
 *
 * {@link MyLayoutManager3}:
 * 只实现了简单的 onLayoutChildren() 和 scrollVerticallyBy() 的竖向 LayoutManager，实现了 view 的回收，可以竖向滑动
 * 页面加载后 adapter 会先调用 50 次 onCreateViewHolder() 和 onBindViewHolder()，然后调用 8 次 onCreateViewHolder() 和 13 次 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 38 个
 * 在滑动中获取到的 RecyclerView 的子 view 数是 13、14 个
 *
 * {@link MyLinearLayoutManager}:
 * 继承自 LinearLayoutManager
 * 页面加载后 adapter 会调用 13 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 13 个
 * 在滑动中获取到的 RecyclerView 的子 view 数有 13、14 个
 *
 * {@link MyFlowLayoutManager}:
 * 流式布局，修改自 {@link com.cashow.layoutmanagerdemo.layoutmanager.FlowLayoutManager}
 * 页面加载后 adapter 会调用 13 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 13 个
 * 在滑动中获取到的 RecyclerView 的子 view 数有 13、14 个
 *
 * {@link MyHorizontalLayoutManager}:
 * 横向的 LayoutManager，修改自 {@link MyFlowLayoutManager}
 * 页面加载后 adapter 会调用 3 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 2 个
 * 在滑动中获取到的 RecyclerView 的子 view 数有 3 个
 *
 * {@link MyAnimHorizontalLayoutManager}:
 * 带有滑动效果的横向的 LayoutManager，修改自 {@link MyHorizontalLayoutManager}
 * 页面加载后 adapter 会调用 3 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 2 个
 * 在滑动中获取到的 RecyclerView 的子 view 数有 3 个
 *
 * {@link MyAnimHorizontalLayoutManager2}:
 * 带有滑动效果的横向的 LayoutManager，修改自 {@link MyHorizontalLayoutManager}
 * 页面加载后 adapter 会调用 3 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 2 个
 * 在滑动中获取到的 RecyclerView 的子 view 数有 3 个
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    MyAdapter myAdapter;
    MyFlowAdapter myFlowAdapter;
    MyImageAdapter myImageAdapter;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        myAdapter = new MyAdapter();
        myFlowAdapter = new MyFlowAdapter();
        myImageAdapter = new MyImageAdapter();

//        setMyLayoutManager();
//        setMyLayoutManager2();
//        setMyLayoutManager3();
//        setMyLinearLayoutManager();
//        setMyFlowLayoutManager();
//        setMyHorizontalLayoutManager();
//        setMyGalleryLayoutManager();
//        setMyAnimHorizontalLayoutManager();
        setMyAnimHorizontalLayoutManager2();

        recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                MLog.d("onScrollChange : " + recyclerview.getChildCount());
            }
        });
    }

    private void setMyGalleryLayoutManager() {
        recyclerview.setLayoutManager(new MyGalleryLayoutManager());
        recyclerview.setAdapter(myImageAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerview);
    }

    private void setMyHorizontalLayoutManager() {
        recyclerview.setLayoutManager(new MyHorizontalLayoutManager());
        recyclerview.setAdapter(myImageAdapter);
    }

    private void setMyAnimHorizontalLayoutManager() {
        recyclerview.setLayoutManager(new MyAnimHorizontalLayoutManager());
        recyclerview.setAdapter(myImageAdapter);
    }

    private void setMyAnimHorizontalLayoutManager2() {
        recyclerview.setLayoutManager(new MyAnimHorizontalLayoutManager2());
        recyclerview.setAdapter(myImageAdapter);
    }

    private void setMyFlowLayoutManager() {
        recyclerview.setLayoutManager(new MyFlowLayoutManager());
        recyclerview.setAdapter(myAdapter);
    }

    private void setMyLinearLayoutManager() {
        recyclerview.setLayoutManager(new MyLinearLayoutManager(context));
        recyclerview.setAdapter(myAdapter);
    }

    private void setMyLayoutManager3() {
        recyclerview.setLayoutManager(new MyLayoutManager3());
        recyclerview.setAdapter(myAdapter);
    }

    private void setMyLayoutManager2() {
        recyclerview.setLayoutManager(new MyLayoutManager2());
        recyclerview.setAdapter(myAdapter);
    }

    private void setMyLayoutManager() {
        recyclerview.setLayoutManager(new MyLayoutManager());
        recyclerview.setAdapter(myAdapter);
    }
}
