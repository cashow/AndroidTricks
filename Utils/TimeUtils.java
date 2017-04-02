package com.cashow.utils;

import android.text.format.Time;

/**
 * 时间处理相关的Utils
 */
public class TimeUtils {
    /**
     * 时间戳转成字符串的时间，有以下几种格式：
     * 刚刚
     * x 秒前
     * x 分钟前
     * x 小时前
     * x 天前
     * x 月 y 日
     * x 年前
     */
    public static String timestampToTimelineTime(long timestamp) {
        if (timestamp > System.currentTimeMillis()) {
            return "刚刚";
        }

        long diffTime = (System.currentTimeMillis() - timestamp) / 1000;
        if (diffTime < 60) {
            return "刚刚";
        }
        diffTime /= 60;
        if (diffTime < 60) {
            return diffTime + "分钟前";
        }
        diffTime /= 60;
        if (diffTime < 24) {
            return diffTime + "小时前";
        }
        diffTime /= 24;
        if (diffTime < 30) {
            return diffTime + "天前";
        }

        Time currentTime = new Time();
        Time mTime = new Time();

        currentTime.setToNow();
        mTime.set(timestamp);

        int mYear = mTime.year;
        int mMonth = mTime.month + 1;
        int mDay = mTime.monthDay;
        int mHour = mTime.hour;
        int mMinute = mTime.minute;
        int mSecond = mTime.second;

        int currentYear = currentTime.year;
        int currentMonth = currentTime.month + 1;
        int currentDay = currentTime.monthDay;
        int currentHour = currentTime.hour;
        int currentMinute = currentTime.minute;
        int currentSecond = currentTime.second;

        if (mYear != currentYear) {
            return (currentYear - mYear) + "年前";
        }
        if (mMonth != currentMonth) {
            return mMonth + "月" + mDay + "日";
        }
        if (mDay != currentDay) {
            return (currentDay - mDay) + "天前";
        }
        if (mHour != currentHour) {
            return (currentHour - mHour) + "小时前";
        }
        if (mMinute != currentMinute) {
            return (currentMinute - mMinute) + "分钟前";
        }
        return (currentSecond - mSecond) + "秒前";
    }

    /**
     * 时间戳转成字符串的时间，有以下几种格式：
     * 刚刚
     * x 分钟前
     * x 小时前
     * x 天前
     * x 月前
     * x 年前
     */
    public static String timestampToPostTime(long timestamp) {
        long diffTime = (System.currentTimeMillis() - timestamp) / 1000;
        if (diffTime < 60) {
            return "刚刚";
        }
        diffTime /= 60;
        if (diffTime < 60) {
            return diffTime + "分钟前";
        }
        diffTime /= 60;
        if (diffTime < 24) {
            return diffTime + "小时前";
        }
        diffTime /= 24;
        if (diffTime < 30) {
            return diffTime + "天前";
        }
        diffTime /= 30;
        if (diffTime < 12){
            return diffTime + "月前";
        }
        diffTime /= 12;
        return diffTime + "年前";
    }

    /**
     * 时间戳转成字符串的时间，有以下几种格式：
     * xx:yy
     * 昨天 xx:yy
     * 星期a xx:yy
     * a 月 b 日 xx:yy
     * a 年 b 月 c 日 xx:yy
     */
    public static String timestampToChatTime(long timestamp) {
        long diffSecond = (System.currentTimeMillis() - timestamp) / 1000;
        long diffMinute = diffSecond / 60;
        long diffHour = diffMinute / 60;
        long diffDay = diffHour / 24;

        Time currentTime = new Time();
        Time mTime = new Time();

        currentTime.setToNow();
        mTime.set(timestamp);

        int mYear = mTime.year;
        int mMonth = mTime.month + 1;
        int mDay = mTime.monthDay;
        int mHour = mTime.hour;
        int mMinute = mTime.minute;
        int mWeekDay = mTime.weekDay;

        int currentYear = currentTime.year;
        int currentDay = currentTime.monthDay;
        int currentWeekDay = currentTime.weekDay;

        String chatTime = mHour + ":";
        if (mMinute < 10) {
            chatTime += "0" + mMinute;
        } else {
            chatTime += mMinute;
        }

        if (diffHour < 24 && mDay == currentDay) {
            return chatTime;
        } else if (diffHour < 48 && mDay == currentDay - 1) {
            return "昨天 " + chatTime;
        } else if (diffDay < 7 && (currentDay - mDay) < 7 && mWeekDay < currentWeekDay) {
            return getWeekDayString(mWeekDay) + " " + chatTime;
        } else if (diffDay < 365 && currentYear == mYear) {
            return mMonth + "月" + mDay + "日 " + chatTime;
        } else {
            return mYear + "年" + mMonth + "月" + mDay + "日 " + chatTime;
        }
    }

    /**
     * 根据本周的第几天返回星期几的字符串
     */
    public static String getWeekDayString(int weekDay) {
        switch (weekDay) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期天";
            default:
                return "星期日";
        }
    }

}
