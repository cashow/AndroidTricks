package com.cashow.articleeditor.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.lang.reflect.Field;

public class KeyboardManager {

    // 状态栏的高度
    private int statusBarHeight;
    // 软键盘的高度
    private int keyboardHeight;
    // 底部虚拟按键的高度
    private int softButtonsBarHeight;
    // 软键盘的显示状态
    private boolean isShowKeyboard;

    private View rootView;

    private KeyboardListener keyboardListener;

    public KeyboardManager() {
        keyboardHeight = -1;
    }

    public void addKeyboardHeightListener(Activity activity, KeyboardListener keyboardListener) {
        statusBarHeight = getStatusBarHeight(activity);
        softButtonsBarHeight = getSoftButtonsBarHeight(activity);
        rootView = getRootFramelayout(activity);
        this.keyboardListener = keyboardListener;

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }

    public void removeKeyboardHeightListener() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            rootView.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
        } else {
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        }
    }

    /**
     * 获取软键盘的高度，如果返回 -1 表示没有获取到软键盘的高度
     */
    public int getKeyboardHeight() {
        return keyboardHeight;
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            // 应用可以显示的区域。此处包括应用占用的区域，
            // 以及ActionBar和状态栏，可能会包含设备底部的虚拟按键。
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);

            // 屏幕高度
            int screenHeight = rootView.getRootView().getHeight();

            int heightDiff = screenHeight - (r.bottom - r.top);

            // 在不显示软键盘时，heightDiff等于 状态栏 + 虚拟按键 的高度
            // 在显示软键盘时，heightDiff会变大，等于 软键盘 + 状态栏 + 虚拟按键 的高度。
            // 所以heightDiff大于 状态栏 + 虚拟按键 高度时表示软键盘出现了，
            // 这时可算出软键盘的高度，即heightDiff减去 状态栏 + 虚拟按键 的高度
            if (keyboardHeight == -1) {
                if (heightDiff > statusBarHeight + softButtonsBarHeight) {
                    keyboardHeight = heightDiff - statusBarHeight - softButtonsBarHeight;
                }
            }

            if (isShowKeyboard) {
                // 如果软键盘是弹出的状态，并且heightDiff小于等于 状态栏 + 虚拟按键 高度，
                // 说明这时软键盘已经收起
                if (heightDiff <= statusBarHeight + softButtonsBarHeight) {
                    isShowKeyboard = false;
                    if (keyboardListener != null) {
                        keyboardListener.onHideKeyboard();
                    }
                }
            } else {
                // 如果软键盘是收起的状态，并且heightDiff大于 状态栏 + 虚拟按键 高度，
                // 说明这时软键盘已经弹出
                if (heightDiff > statusBarHeight + softButtonsBarHeight) {
                    isShowKeyboard = true;
                    if (keyboardListener != null) {
                        keyboardListener.onShowKeyboard();
                    }
                }
            }
        }
    };

    public boolean isShowKeyboard() {
        return isShowKeyboard;
    }

    /**
     * 显示软键盘
     */
    public static void showSoftKeyBoard(Activity mActivity, EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyBoard(Activity mActivity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 获取activity顶层的view
     */
    private static FrameLayout getRootFramelayout(Activity activity) {
        return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    /**
     * 获取状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取底部虚拟按键的高度
     */
    public static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight)
            return realHeight - usableHeight;
        else
            return 0;
    }
}
