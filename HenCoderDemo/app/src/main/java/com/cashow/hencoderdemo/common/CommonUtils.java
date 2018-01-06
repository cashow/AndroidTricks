package com.cashow.hencoderdemo.common;

import android.content.Context;

public class CommonUtils {
    /**
     * 将dp转换成px值
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
