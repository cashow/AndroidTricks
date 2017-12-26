package com.cashow.constraintlayoutdemo;

import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * 相关链接：http://blog.csdn.net/guolin_blog/article/details/53122387
 *
 * layout_1 :
 * button1 居中显示
 * button2 位于 button1 下方，并且和 button1 在垂直方向居中
 *
 * layout_2 :
 * button1 在水平 25% 垂直 25% 的位置
 * button2 位于 button1 下方，并且和 button1 在垂直方向居中
 *
 * layout_3 :
 * button1 在水平 25% 垂直 25% 的位置
 * button2 位于 button1 下方，宽度和高度都是 match constraints
 * button3 位于 button1 右边，宽度是 match constraints
 *
 * layout_4 :
 * 添加一个水平居中的 Guideline
 * button1 在 Guideline 的左边
 * button2 在 Guideline 的右边
 *
 * layout_5 :
 * infer constraints
 *
 * layout_6 :
 * textView1 和 textView2 的 base line 对齐
 *
 * layout_7 :
 * 宽度 200dp 并宽高比是 1：1 的 ImageView
 *
 * layout_8 :
 * 宽度和高度都是 0dp 时，会在满足约束条件和比率的情况下占用最大尺寸
 *
 * layout_9 :
 * 宽度和高度都是 0dp，最大尺寸是父控件的亮度，宽高比是 2：1 和 1：2
 *
 * layout_10 :
 * 圆形定位
 *
 * layout_11 :
 * 限定 MATCH_CONSTRAINT 控件所占的百分比
 *
 * layout_12 :
 * 两个控件链接起来
 *
 * layout_13 :
 * group
 *
 * layout_14 :
 * barrier
 *
 * layout_15 :
 * goneMargin
 */
public class MainActivity extends AppCompatActivity {
    @BindViews({R.id.layout_1, R.id.layout_2, R.id.layout_3, R.id.layout_4,
            R.id.layout_5, R.id.layout_6, R.id.layout_7, R.id.layout_8,
            R.id.layout_9, R.id.layout_10, R.id.layout_11, R.id.layout_12,
            R.id.layout_13, R.id.layout_14, R.id.layout_15})
    List<View> layoutList;

    Group groupButton;
    Group groupImage;

    private int currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        currentLayout = 0;

        initGroup();
    }

    private void initGroup() {
        groupButton = layoutList.get(12).findViewById(R.id.group_button);
        groupImage = layoutList.get(12).findViewById(R.id.group_image);

        int buttonIds[] = groupButton.getReferencedIds();

        for (int buttonId : buttonIds) {
            layoutList.get(12).findViewById(buttonId).setOnClickListener(v -> {
                groupImage.setVisibility(groupImage.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        layoutList.get(currentLayout).setVisibility(View.GONE);
        switch (item.getItemId()) {
            case R.id.menu1:
                currentLayout = 0;
                break;
            case R.id.menu2:
                currentLayout = 1;
                break;
            case R.id.menu3:
                currentLayout = 2;
                break;
            case R.id.menu4:
                currentLayout = 3;
                break;
            case R.id.menu5:
                currentLayout = 4;
                break;
            case R.id.menu6:
                currentLayout = 5;
                break;
            case R.id.menu7:
                currentLayout = 6;
                break;
            case R.id.menu8:
                currentLayout = 7;
                break;
            case R.id.menu9:
                currentLayout = 8;
                break;
            case R.id.menu10:
                currentLayout = 9;
                break;
            case R.id.menu11:
                currentLayout = 10;
                break;
            case R.id.menu12:
                currentLayout = 11;
                break;
            case R.id.menu13:
                currentLayout = 12;
                break;
            case R.id.menu14:
                currentLayout = 13;
                break;
            case R.id.menu15:
                currentLayout = 14;
                break;
        }
        layoutList.get(currentLayout).setVisibility(View.VISIBLE);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout_menu, menu);
        return true;
    }
}
