package com.cashow.constraintlayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
