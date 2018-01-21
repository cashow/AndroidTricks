package com.cashow.hencoderdemo.chapter_2_practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cashow.hencoderdemo.common.BaseActivity;

import org.adrianwalker.multilinestring.Multiline;

import java.util.ArrayList;
import java.util.List;

public class Chapter_2_Practice_Activity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addMyDemoView(getTagList1(), INFO_0);
        addMyDemoView(getTagList2(), INFO_1);
        addMyDemoView(getTagList3(), INFO_2);
    }

    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{};
    }

    private View getTagList1() {
        NewTagGroup newTagGroup = new NewTagGroup(context);
        List<String> tagList = new ArrayList<>();
        tagList.add("tag1");
        tagList.add("tag111");
        tagList.add("tag111111");
        tagList.add("tag111111111");
        tagList.add("tag1111");
        tagList.add("tag11");
        tagList.add("tag111111111111");
        tagList.add("tag111111111");
        tagList.add("tag1111111");
        tagList.add("tag1111");
        tagList.add("tag11");
        tagList.add("tag1111");
        tagList.add("tag11");
        tagList.add("tag1");
        newTagGroup.setTagList(tagList);
        return newTagGroup;
    }

    private View getTagList2() {
        NewTagGroup newTagGroup = new NewTagGroup(context);
        List<String> tagList = new ArrayList<>();
        tagList.add("tag2");
        tagList.add("tag222");
        tagList.add("tag222222");
        tagList.add("tag222222222");
        tagList.add("tag2222");
        tagList.add("tag22");
        tagList.add("tag222222222222");
        tagList.add("tag222222222");
        tagList.add("tag2222222");
        tagList.add("tag2222");
        tagList.add("tag22");
        tagList.add("tag2222");
        tagList.add("tag22");
        tagList.add("tag2");
        newTagGroup.setTagList(tagList);
        newTagGroup.setMaxLineNumber(1);
        return newTagGroup;
    }

    private View getTagList3() {
        NewTagGroup newTagGroup = new NewTagGroup(context);
        List<String> tagList = new ArrayList<>();
        tagList.add("tag3");
        tagList.add("tag333");
        tagList.add("tag333333");
        tagList.add("tag333333333");
        tagList.add("tag3333");
        tagList.add("tag33");
        tagList.add("tag333333333333");
        tagList.add("tag333333333");
        tagList.add("tag3333333");
        tagList.add("tag3333");
        tagList.add("tag33");
        tagList.add("tag3333");
        tagList.add("tag33");
        tagList.add("tag3");
        newTagGroup.setTagList(tagList);
        newTagGroup.setMaxLineNumber(2);
        return newTagGroup;
    }

    /**
     * 继承自 ViewGroup 的标签组，实现了 onMeasure() 和 onLayout() 方法
     */
    @Multiline
    static String INFO_0;

    /**
     * 最大行数 : 1
     */
    @Multiline
    static String INFO_1;

    /**
     * 最大行数 : 2
     */
    @Multiline
    static String INFO_2;
}
