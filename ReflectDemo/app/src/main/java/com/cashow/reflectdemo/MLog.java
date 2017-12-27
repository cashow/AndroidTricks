package com.cashow.reflectdemo;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

/**
 * 打印 log，如果 log 的内容过长，会分段打印出来
 */
public class MLog{
    /**
     * 调用 MLog 的代码所在的类名
     */
    private static String className;
    /**
     * 调用 MLog 的代码所在的方法名
     */
    private static String methodName;
    /**
     * 调用 MLog 的代码所在的行数
     */
    private static int lineNumber;

    /**
     * 上一次调用 MLog.t() 的时间
     */
    private static long lastLogTime;

    /**
     * 日志的默认标签
     */
    private static String TAG = "MLog";

    /**
     * 日志内容如果超过 MAX_LOG_LENGTH 将分段打印
     */
    private static final int MAX_LOG_LENGTH = 200;

    /**
     * 标签是 tag，内容是 text
     */
    public static void d(String tag, String text) {
        printLog(tag, text);
    }

    /**
     * 标签是默认的 TAG，内容是 text
     */
    public static void d(String text) {
        printLog(TAG, text);
    }

    /**
     * 标签是默认的 TAG，内容是调用 MLog 的代码所在的类名、方法名和行数
     */
    public static void d() {
        getMethodNames(new Throwable().getStackTrace());
        printLog(TAG, String.format(Locale.CHINA, "【%s】 %s() line %d", className, methodName, lineNumber));
    }

    /**
     * 标签是默认的 TAG，内容是调用 MLog 的代码所在的类名、方法名和行数，以及与上一次调用 MLog.t() 的时间间隔
     */
    public static void t() {
        getMethodNames(new Throwable().getStackTrace());
        long currentTime = System.currentTimeMillis();
        if (lastLogTime == 0) {
            lastLogTime = currentTime;
        }
        printLog(TAG, String.format(Locale.CHINA, "【%s】 %s() line %d : %fs", className, methodName, lineNumber, (currentTime - lastLogTime) / 1000.0f));
        lastLogTime = currentTime;
    }

    /**
     * 标签是默认的 TAG，内容是将 object 解析后的 json 字符串
     */
    public static void d(Object object) {
        String text = new Gson().toJson(object);
        printLog(TAG, text);
    }

    /**
     * 标签是默认的 TAG，内容是 【prefix】 加上 object 解析后的 json 字符串
     */
    public static void d(String prefix, Object object) {
        printLog(TAG, "【" + prefix + "】");
        printLog(TAG, new Gson().toJson(object));
    }

    /**
     * 标签是默认的 TAG，内容是【prefix】加上 Exception 里的 StackTrace 转换成的字符串
     */
    public static void d(String prefix, Exception e) {
        printLog(TAG, "【" + prefix + "】: " + getStackTrace(e));
    }

    /**
     * 标签是默认的 TAG，内容是【prefix】加上 Throwable 里的 StackTrace 转换成的字符串
     */
    public static void d(String prefix, Throwable e) {
        printLog(TAG, "【" + prefix + "】: " + getStackTrace(e));
    }

    /**
     * 获取调用 MLog 的代码所在的类名、方法名和行数
     */
    private static void getMethodNames(StackTraceElement[] sElements){
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    /**
     * @param tag: 日志的标签
     * @param text: 日志的内容
     */
    private static void printLog(String tag, String text) {
        if (text == null || TextUtils.isEmpty(text)) {
            text = "";
        }
        while(!TextUtils.isEmpty(text)) {
            String sub_str = text.substring(0, Math.min(MAX_LOG_LENGTH, text.length()));
            Log.d(tag, sub_str);
            text = text.substring(sub_str.length(), text.length());
        }
    }

    /**
     * 将 Throwable 转成字符串
     */
    private static String getStackTrace(Throwable e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
