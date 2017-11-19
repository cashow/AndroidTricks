package com.cashow.lifecycledemo;

import android.text.TextUtils;
import android.util.Log;

/**
 * 打印log
 */
public class MLog {
    static String className;
    static String methodName;

    private static String TAG = "cashow";

    public static void d(String text) {
        printLog(TAG, text);
    }

    public static void d() {
        getMethodNames(new Throwable().getStackTrace());
        printLog(TAG, className + " " + methodName);
    }

    private static void getMethodNames(StackTraceElement[] sElements){
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
    }

    /**
     * @param tag: log的标签
     * @param text: log的内容
     */
    private static void printLog(String tag, String text) {
        if (text == null || TextUtils.isEmpty(text)) {
            text = "";
        }
        Log.d(tag, text);
    }
}
