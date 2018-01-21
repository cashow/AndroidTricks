package com.cashow.hencoderdemo.chapter_1_practice;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cashow.hencoderdemo.common.BaseActivity;

import org.adrianwalker.multilinestring.Multiline;

public class Chapter_1_Practice_Activity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addMyDemoView(new GeometryDashDemoView(context), INFO_0);
        addMyDemoView(new FlipView(context, 0), INFO_1);
    }

    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{};
    }

    /**
     * 模仿 Geometry Dash 游戏的 demo
     */
    @Multiline
    static String INFO_0;

    /**
     *
     */
    @Multiline
    static String INFO_1;
}
