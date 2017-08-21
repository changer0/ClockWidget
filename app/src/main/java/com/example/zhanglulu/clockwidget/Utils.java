package com.example.zhanglulu.clockwidget;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.Context;
import android.content.pm.ResolveInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;


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
}
