package com.example.zhanglulu.clockwidget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.widget.TextView;

import com.example.zhanglulu.clockwidget.chinesecalendar.LunarSolarConverter;

import java.util.ArrayList;


/**
 * Created by lulu on 2017/8/20.
 */

public class Utils {
    //--------------------------------
    //获取文本大小
    public static int getTextSize(String key, Context context) {
        Context con = context.getApplicationContext();
        SharedPreferences sp = con.getSharedPreferences(ClockWidget.CLOCK_WIDGET, Context.MODE_PRIVATE);
        int defaultSize = 30;
        if (key.equals(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_TIME)) {
            defaultSize = con.getResources().getDimensionPixelSize(R.dimen.time_default_size);
        } else if (key.equals(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_DATE)) {
            defaultSize = con.getResources().getDimensionPixelOffset(R.dimen.date_default_size);
        } else if (key.equals(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_WEEK)) {
            defaultSize = con.getResources().getDimensionPixelOffset(R.dimen.week_default_size);
        } else if (key.equals(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_CHINESE)){
            defaultSize = con.getResources().getDimensionPixelOffset(R.dimen.chinese_default_size);
        }
        return sp.getInt(key, defaultSize);
    }
    //保存文本大小
    public static void saveTextSize(String key, int size, Context context) {
        SharedPreferences sp = context.getSharedPreferences(ClockWidget.CLOCK_WIDGET, Context.MODE_PRIVATE);
        sp.edit().putInt(key, size).apply();
    }

    /**
     * 重置文本大小
     * @param context
     */
    public static void resetTextSize(Context context) {
        Context con = context.getApplicationContext();
        saveTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_TIME, con.getResources().getDimensionPixelSize(R.dimen.time_default_size), context);
        saveTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_DATE, con.getResources().getDimensionPixelSize(R.dimen.date_default_size), context);
        saveTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_WEEK, con.getResources().getDimensionPixelSize(R.dimen.week_default_size), context);
        saveTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_CHINESE, con.getResources().getDimensionPixelSize(R.dimen.chinese_default_size), context);
    }

    //------------------------


    // 使用12进制
    public static boolean isUse12Mode(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(ClockWidget.CLOCK_WIDGET, Context.MODE_PRIVATE);
        return sp.getBoolean(ClockWidget.CLOCK_WIDGET_IS_USE_12_MODE, false);
    }

    public static void setUse12Mode(Context ctx, boolean isUse12Mode) {
        SharedPreferences sp = ctx.getSharedPreferences(ClockWidget.CLOCK_WIDGET, Context.MODE_PRIVATE);
        sp.edit().putBoolean(ClockWidget.CLOCK_WIDGET_IS_USE_12_MODE, isUse12Mode).apply();
    }

    public static int get12ModeTime(int currentHour, Context ctx) {
        int ret = currentHour;
        if (currentHour > 12 && isUse12Mode(ctx)) {
            ret = currentHour -12;
        }
        return ret;
    }

    public static String get12ModeText(int currentHour, Context ctx) {
        String ret = "";
        if (isUse12Mode(ctx)) {
            if (currentHour > 12) {
                ret = "pm";
            } else {
                ret = "am";
            }
        }
        return ret;
    }

    //判断是否含有当前Activity
    public static boolean isHasActivity(String className,String pageName,  Context ctx) {
        ArrayList<String> result = new ArrayList<String>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setPackage(pageName);
        for (ResolveInfo info : ctx.getPackageManager().queryIntentActivities(intent, 0)) {
            result.add(info.activityInfo.name);
        }
        return result.contains(className);
    }

    public static String convertGanTime(int hour) {
        final String[] diZhi =   {"子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥"};
        int index = (hour + 1) / 2;
        if (index >= diZhi.length) {
            index = diZhi.length-1;
        }
        return diZhi[index] + "时";
    }


    /**
     *
     * @param lunarMonth 农历月份
     * @return 天干地支月份
     */
    public static String convertGanZhiMonth(int lunarYear, int lunarMonth) {
        String ganZhiMonth = "";
        String ganZhiYear = converGanZhiYear(lunarYear);

        final String[] tianGan = {"甲","乙","丙","丁","戊","己","庚","辛","壬","癸","甲","乙"};
        final String[] diZhi =   {"寅","卯","辰","巳","午","未","申","酉","戌","亥","子","丑"};
        int index = 0;
        if (ganZhiYear.contains("戊") || ganZhiYear.contains("癸")) {
            index = 0;
        } else if (ganZhiYear.contains("丁") || ganZhiYear.contains("壬")) {
            index = 2;
        } else if (ganZhiYear.contains("丙") || ganZhiYear.contains("辛")) {
            index = 4;
        } else if (ganZhiYear.contains("乙") || ganZhiYear.contains("庚")) {
            index = 6;
        } else {
            index = 8;
        }
        ganZhiMonth = tianGan[((lunarMonth-1) - index + 12) % 12];
        return ganZhiMonth + diZhi[lunarMonth-1];
    }

    /**
     *
     * @param lunarYear 农历年份
     * @return 天干地支年份
     */
    public static String converGanZhiYear(int lunarYear) {
        return LunarSolarConverter.lunarYearToGanZhi(lunarYear);
    }
}
