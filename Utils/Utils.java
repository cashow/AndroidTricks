package com.cashow.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Locale;

import static com.cashow.utils.FileUtils.getFilePathByContentResolver;


public class Utils {

    /**
     * 获取android唯一标识
     * http://cloudstack.blog.163.com/blog/static/1876981172012710823152/
     */
    public static String getAndroidUid(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String szImei = "";
        try {
            szImei = TelephonyMgr.getDeviceId();
        } catch (Exception e) {
        }
        String m_szDevIDShort = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length()
                % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
                + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10
                + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
                + Build.USER.length() % 10;
        String m_szAndroidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        String m_szWLANMAC = "";
        try {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
        }
        String m_szLongID = szImei + m_szDevIDShort + m_szAndroidID + m_szWLANMAC;
        // compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = "";
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper
            // padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        } // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase(Locale.getDefault());
        return m_szUniqueID;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
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
     * 保存图片后通知系统刷新相册
     */
    public static void refreshSystemAlbum(Context context, String uriPath) {

        Uri uri = Uri.parse(uriPath);
        String path = getFilePathByContentResolver(context, uri);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uriFromFile = Uri.fromFile(new File(path));
        intent.setData(uriFromFile);
        context.sendBroadcast(intent);
    }

    /**
     * 对字符串取md5
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    /**
     * 判断字符串是不是空，空返回false，不是空返回true
     */
    public static boolean isStringNotNull(String string) {
        if (string == null)
            return false;
        if (TextUtils.isEmpty(string))
            return false;
        return true;
    }

    /**
     * 判断字符串是不是空，空返回true，不是空返回false
     */
    public static boolean isStringNull(String string) {
        if (string == null)
            return true;
        if (TextUtils.isEmpty(string))
            return true;
        return false;
    }

    /**
     * 判断Collection是不是空，空返回false，不是空返回true
     */
    public static boolean isCollectionNotNull(Collection collection){
        if (collection != null && collection.size() > 0)
            return true;
        return false;
    }

    /**
     * 判断Collection是不是空，空返回true，不是空返回false
     */
    public static boolean isCollectionNull(Collection collection){
        if (collection == null || collection.size() == 0)
            return true;
        return false;
    }

    /**
     * 获取app的versionName
     */
    public static String getVersionName(Context context) throws Exception {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 获取app的versionCode
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将px转换成dp值
     */
    public static int px2dp(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }

    /**
     * 将dp转换成px值
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 将sp转换成px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取app目前的内存占用情况
     */
    public static String getMemoryInfo() {
        String memoryInfo = "";
        try {
            Runtime info = Runtime.getRuntime();
            long freeSize = info.freeMemory();
            long totalSize = info.totalMemory();
            long maxSize = info.maxMemory();
            double freeMemory = freeSize / 1048576.0;
            double totalMemory = totalSize / 1048576.0;
            double maxMemory = maxSize / 1048576.0;
            String freeMemoryString = String.valueOf(freeMemory).substring(0, 5);
            String totalMemoryString = String.valueOf(totalMemory).substring(0, 5);
            String maxMemoryString = String.valueOf(maxMemory).substring(0, 5);

            memoryInfo = freeMemoryString + "M/" + totalMemoryString + "M(" + maxMemoryString + "M)";
        } catch (Exception e) {
        }
        return memoryInfo;
    }

    /**
     * 粗略检查字符串是不是手机号
     */
    public static boolean checkPhoneNum(String phoneNum) {
        phoneNum = phoneNum.replace("-", "");
        phoneNum = phoneNum.replace("+86", "");
        phoneNum = phoneNum.replace(" ", "");

        if (phoneNum.matches(""))
            return false;
        if (!phoneNum.substring(0, 1).equals("1"))
            return false;
        if (phoneNum.length() != 11)
            return false;
        for (int i = phoneNum.length(); --i >= 0; ) {
            if (!Character.isDigit(phoneNum.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取手机的sdk版本号
     */
    public static int getApiLevel() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 检查是否有相机
     */
    public static boolean checkCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查是否有前置摄像头
     */
    public static boolean checkFrontCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 将view转成bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {
        // Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        // Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        // Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            // has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        // draw the view on the canvas
        view.draw(canvas);
        // return the bitmap
        return returnedBitmap;
    }

    /**
     * 判断触摸点是不是在view上
     */
    public static boolean isInView(View view, float rawX, float rawY) {
        int scrcoords[] = new int[2];
        view.getLocationOnScreen(scrcoords);
        float x = rawX + view.getLeft() - scrcoords[0];
        float y = rawY + view.getTop() - scrcoords[1];
        if (x >= view.getLeft() && x <= view.getRight() && y >= view.getTop() && y <= view.getBottom())
            return true;
        return false;
    }

    /**
     * 根据ListView的下标获取对应的view
     */
    public static View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    /**
     * 获取域名
     */
    public static String getDomain(String host) {
        URL url = null;
        try {
            url = new URL(host);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return url.getHost();
    }

    /**
     * 判断是不是汉字
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断是不是英文字符
     */
    public static boolean isEnglish(char c) {
        if (c >= 'a' && c <= 'z')
            return true;
        if (c >= 'A' && c <= 'Z')
            return true;
        return false;
    }

    /**
     * 判断是不是数字字符
     */
    public static boolean isNumber(char c) {
        if (c >= '0' && c <= '9')
            return true;
        return false;
    }

    /**
     * 检测String是否全是中文、英文和数字
     */
    public static boolean checkText(String text) {
        boolean res = true;
        char[] cTemp = text.toCharArray();
        for (int i = 0; i < text.length(); i++) {
            if (!isChinese(cTemp[i]) && !isEnglish(cTemp[i]) && !isNumber(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * 计算字符串要占用的宽度
     */
    public static float getTextSize(TextView tagName, String text) {
        Paint paint = new Paint();
        tagName.setText(text);
        paint.setTextSize(tagName.getTextSize());
        float text_width = paint.measureText(text);
        return text_width;
    }

    /**
     * 设置控件的宽度和高度
     */
    public static void setWidthAndHeight(View view, int width, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        lp.height = height;
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
}
