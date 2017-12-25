package com.cashow.databindingdemo.util;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

/**
 * 打印log，对于长的log可以分段打印出来
 */
public class MLog {
    static String className;
    static String methodName;
    static int lineNumber;

    private static long lastLogTime;

    private static String TAG = "haomaiyi";

    private static final int MAX_LOG_LENGTH = 200;

    private static final int DEBUG = 0;
    private static final int ERROR = 1;

    public static void d(String tag, String text) {
        printLog(tag, text, DEBUG);
    }

    public static void d(String text) {
        printLog(TAG, text, DEBUG);
    }

    public static void d() {
        getMethodNames(new Throwable().getStackTrace());
        printLog(TAG, className + " " + methodName + " " + lineNumber, DEBUG);
    }

    public static void t() {
        getMethodNames(new Throwable().getStackTrace());
        long currentTime = System.currentTimeMillis();
        if (lastLogTime == 0) {
            lastLogTime = currentTime;
        }
        printLog(TAG, String.format(Locale.CHINA, "[%s] %s() line %d : %d", className, methodName, lineNumber, (currentTime - lastLogTime) / 1000), DEBUG);
        lastLogTime = currentTime;
    }

    public static void d(String prefix, Exception e) {
        printLog(TAG, "【" + prefix + "】: " + getStackTrace(e), DEBUG);
    }

    public static void d(String prefix, Throwable e) {
        printLog(TAG, "【" + prefix + "】: " + getStackTrace(e), DEBUG);
    }

    public static void e(String tag, String text) {
        printLog(tag, text, ERROR);
    }

    public static void e(String text) {
        printLog(TAG, text, ERROR);
    }

    private static void getMethodNames(StackTraceElement[] sElements){
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    /**
     * @param tag: log的标签
     * @param text: log的内容
     * @param debugType: log的类型，DEBUG表示Log.d(), ERROR表示Log.e()
     */
    private static void printLog(String tag, String text, int debugType) {
        if (text == null || TextUtils.isEmpty(text)) {
            text = "";
        }
        while(!TextUtils.isEmpty(text)) {
            if (Build.BRAND.equals("HONOR")) {
                debugType = ERROR;
            }
            String sub_str = text.substring(0, Math.min(MAX_LOG_LENGTH, text.length()));
            switch (debugType) {
                case DEBUG:
                    Log.d(tag, sub_str);
                    break;
                case ERROR:
                    Log.e(tag, sub_str);
                    break;
            }
            text = text.substring(sub_str.length(), text.length());
        }
    }

    private static String getStackTrace(Throwable e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
