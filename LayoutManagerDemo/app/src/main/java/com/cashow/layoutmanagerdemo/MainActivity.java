package com.cashow.layoutmanagerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cashow.layoutmanagerdemo.layoutmanager.MyAnimHorizontalLayoutManager;
import com.cashow.layoutmanagerdemo.layoutmanager.MyAnimHorizontalLayoutManager2;
import com.cashow.layoutmanagerdemo.layoutmanager.MyFlowLayoutManager;
import com.cashow.layoutmanagerdemo.layoutmanager.MyHorizontalLayoutManager;
import com.cashow.layoutmanagerdemo.layoutmanager.MyLayoutManager;
import com.cashow.layoutmanagerdemo.layoutmanager.MyLayoutManager2;
import com.cashow.layoutmanagerdemo.layoutmanager.MyLayoutManager3;
import com.cashow.layoutmanagerdemo.layoutmanager.MyLinearLayoutManager;
import com.cashow.layoutmanagerdemo.layoutmanager.MySwipeCardLayoutManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

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
 *
 * {@link MySwipeCardLayoutManager}:
 * 堆叠式的 LayoutManager
 * 修改自 https://github.com/mcxtzhang/ZLayoutManager
 *
 * 页面加载后 adapter 会调用 4 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 4 个
 * 在滑动中获取到的 RecyclerView 的子 view 数有 4 个
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_activity_1)
    void onActivity1Click() {
        startActivity(new Intent(this, Activity1.class));
    }

    @OnClick(R.id.button_activity_2)
    void onActivity2Click() {
        startActivity(new Intent(this, Activity2.class));
    }

    @OnClick(R.id.button_activity_3)
    void onActivity3Click() {
        startActivity(new Intent(this, Activity3.class));
    }

    @OnClick(R.id.button_activity_4)
    void onActivity4Click() {
        startActivity(new Intent(this, Activity4.class));
    }

    @OnClick(R.id.button_activity_5)
    void onActivity5Click() {
        startActivity(new Intent(this, Activity5.class));
    }

    @OnClick(R.id.button_activity_6)
    void onActivity6Click() {
        startActivity(new Intent(this, Activity6.class));
    }

    @OnClick(R.id.button_activity_7)
    void onActivity7Click() {
        startActivity(new Intent(this, Activity7.class));
    }

    @OnClick(R.id.button_activity_8)
    void onActivity8Click() {
        startActivity(new Intent(this, Activity8.class));
    }

    @OnClick(R.id.button_activity_9)
    void onActivity9Click() {
        startActivity(new Intent(this, Activity9.class));
    }
}
