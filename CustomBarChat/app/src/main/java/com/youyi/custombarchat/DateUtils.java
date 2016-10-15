package com.youyi.custombarchat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Roy on 16/10/14.
 */
public class DateUtils {
    /**
     * 获取当天的月日（例如：2015-5-27）
     *
     * @return
     */
    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return formatter.format(date);
    }

    public static String getYearToMinTimeStr(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date date = new Date(time);
        return formatter.format(date);
    }

    public static String getYearToMinTimeStr(String longTimeStr) {
        if (longTimeStr != null) {
            longTimeStr = longTimeStr.trim();
            try {
                long time = Long.parseLong(longTimeStr);
                return getYearToMinTimeStr(time);
            } catch (NumberFormatException e) {
                return "";
            }
        }

        return "";
    }

    public static String getYearToDayTimeStr(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = new Date(time);
        return formatter.format(date);
    }

    public static Date getDateByStr(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否为前一天
     *
     * @param otherday
     * @return
     */
    public static Boolean beforeToday(String today, String otherday) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date date = sdf.parse(today);
            Date date1 = sdf.parse(otherday);
            return date1.before(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取哪天的前i天或者后i天
     *
     * @param str
     * @param i
     * @return
     */
    public static String getDate(String str, int i) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date myDate = formatter.parse(str);
            Calendar c = Calendar.getInstance();
            c.setTime(myDate);
            c.add(Calendar.DAY_OF_MONTH, i);
            myDate = c.getTime();
            return formatter.format(myDate);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    /**
     * 获取哪天是星期几
     *
     * @param str
     * @return
     */
    public static CharSequence getWeekByDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date date = sdf.parse(str);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    return "星期日";
                case 2:
                    return "星期一";
                case 3:
                    return "星期二";
                case 4:
                    return "星期三";
                case 5:
                    return "星期四";
                case 6:
                    return "星期五";
                case 7:
                    return "星期六";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "星期日";
    }

    public static String getWeekByDate(int tempDay) {
        if (tempDay > 0 && tempDay < 8) {
            String mWeekday = null;
            switch (tempDay) {
                case 1:
                    mWeekday = "周日";
                    break;
                case 2:
                    mWeekday = "周一";
                    break;
                case 3:
                    mWeekday = "周二";
                    break;
                case 4:
                    mWeekday = "周三";
                    break;
                case 5:
                    mWeekday = "周四";
                    break;
                case 6:
                    mWeekday = "周五";
                    break;
                case 7:
                    mWeekday = "周六";
                    break;
            }
            return mWeekday;
        }
        return "";
    }

    /**
     * 返回DateStr的年、月、日
     *
     * @param str
     * @param need
     * @return
     */
    public static int getTimeByDate(String str, int need) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date date = sdf.parse(str);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(need);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 返回当前是XX年
     *
     * @return
     */
    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.MONTH);
    }

    public static int getDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回传入时间跟今天相差天数
     *
     * @param calendar
     * @return
     */
    public static int getDayDiff(Calendar calendar) {
        int DAY = 1000 * 3600 * 24;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(System.currentTimeMillis());
        long s = calendar.getTimeInMillis();
        long s1 = calendar1.getTimeInMillis();
        long day = s / DAY * DAY;
        long day1 = s1 / DAY * DAY;
        return (int) ((calendar.getTimeInMillis() / DAY * DAY - calendar1.getTimeInMillis() / DAY * DAY) / DAY);
    }

    /**
     * 根据指定年月，获取该月有几天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDayNumOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 判断当前日期是否在7天内
     *
     * @param str
     * @return
     */
    public static boolean isInWeek(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date date = sdf.parse(str);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.DAY_OF_MONTH, -8);
            Date date1 = calendar.getTime();
            return date1.before(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取当天的0时0分0秒的时间戳 ms
     *
     * @return
     */
    public static long getDayTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }

    /**
     * 获取某一天的0时0分0秒的时间戳 ms
     *
     * @param desStr
     * @return
     */
    public static long getSomeDayTimeInMillis(String desStr) {
        long time = 0;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = df.parse(desStr);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.setTime(date);
            time = cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;

    }


    /**
     * 获取当天0时0分0秒
     * 格式为 yy - MM - dd的 Date数据
     *
     * @return
     */
    public static Date getDayDate() {
        long time = getDayTimeInMillis();
        return new Date(time);
    }

    /**
     * 获取本周一的日期 0时0分0秒
     *
     * @param date
     * @return
     */
    public static Date getNowWeekMonday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONTH);
        return calendar.getTime();
    }

    /**
     * 提取指定yyyy-MM-dd日期的月
     *
     * @param str
     * @return
     */
    public static int getMonthByStrNorm(String str) {
        int result = 0;
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            SimpleDateFormat sdf0 = new SimpleDateFormat("MM");
            String str0 = sdf0.format(d1);
            if (Integer.parseInt(str0) < 10) {
                result = Integer.parseInt(str0.substring(1));
            } else {
                result = Integer.parseInt(str0);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 提取指定yyyy-MM-dd日期的日
     *
     * @param str
     * @return
     */
    public static int getDayByStrNorm(String str) {
        int result = 0;
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            SimpleDateFormat sdf0 = new SimpleDateFormat("dd");
            String str0 = sdf0.format(d1);
            if (Integer.parseInt(str0) < 10) {
                result = Integer.parseInt(str0.substring(1));
            } else {
                result = Integer.parseInt(str0);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 本月的1号0时0分0秒 的时间戳 单位ms
     *
     * @return
     */
    public static long getCurMonthTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取下一月的1号0时0分0秒 的时间戳 单位ms
     *
     * @param str
     * @return
     */
    public static long getNextMonthOneTimeInMillis(String str) {
        long time = 0;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = df.parse(str);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.setTime(date);
            time = cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    /**
     * 是否是当天、当周、月
     *
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isCurrent(String type, long startTime, long endTime) {
        long curTime = 0;
        if (type.equals("day")) {
            curTime = DateUtils.getDayTimeInMillis() / 1000;
            if (curTime == startTime) {
                return true;
            }

        } else if (type.equals("week")) {
            //获取本周一的时间戳 对比
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            //本周一日期
            String temp = formatter.format(DateUtils.getNowWeekMonday(DateUtils.getDayDate()));
            //本周周一的0时0分0秒
            curTime = DateUtils.getSomeDayTimeInMillis(temp) / 1000;
            if (curTime == startTime) {
                return true;
            }

        } else if (type.equals("month")) {
            //获取本月1号时间戳对比
            curTime = DateUtils.getCurMonthTimeInMillis() / 1000;
            if (curTime == startTime) {
                return true;
            }
        }
        return false;
    }

}
