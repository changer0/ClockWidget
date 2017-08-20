package com.example.zhanglulu.clockwidget;

import android.content.Context;
import android.content.SharedPreferences;

import android.content.Context;


/**
 * Created by lulu on 2017/8/20.
 */

public class Utils {
    //--------------------------------
    //获取文本大小
    public static int getTextSize(String key, Context context) {
        SharedPreferences sp = context.getSharedPreferences(ClockWidget.CLOCK_WIDGET, Context.MODE_PRIVATE);
        int defaultSize = 30;
        if (key.equals(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_TIME)) {
            defaultSize = context.getResources().getDimensionPixelSize(R.dimen.time_default_size);
        } else if (key.equals(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_DATE)) {
            defaultSize = context.getResources().getDimensionPixelOffset(R.dimen.date_default_size);
        } else if (key.equals(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_WEEK)) {
            defaultSize = context.getResources().getDimensionPixelOffset(R.dimen.week_default_size);
        } else {
            defaultSize = context.getResources().getDimensionPixelOffset(R.dimen.chinese_default_size);
        }
        return sp.getInt(key, defaultSize);
    }
    //保存文本大小
    public static void saveTextSize(String key, int size, Context context) {
        SharedPreferences sp = context.getSharedPreferences(ClockWidget.CLOCK_WIDGET, Context.MODE_PRIVATE);
        sp.edit().putInt(key, size).apply();
    }
}
