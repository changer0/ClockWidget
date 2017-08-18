package com.example.zhanglulu.clockwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhanglulu on 2017/8/16.
 */

public class ClockWidget extends AppWidgetProvider {
    public static final String CLOCK_WIDGET = "clock_widget";
    public static final String CLOCK_WIDGET_BG_COLOR = "clock_widget_bg_color";
    public static final String CLOCK_WIDGET_TEXT_COLOR = "clock_widget_text_color";
    public static final String CLOCK_WIDGET_HAS_TIME = "clock_widget_has_text";
    public static final String CLOCK_WIDGET_HAS_DATA = "clock_widget_has_data";
    public static final String CLOCK_WIDGET_HAS_WEEK = "clock_widget_has_week";
    public static final String CLOCK_WIDGET_HAS_SETTING = "clock_widget_has_setting";
    public static final String CLOCK_WIDGET_HAS_CHINESE = "clock_widget_has_chinese";

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d("lulu", "ClockWidget-onUpdate ");
        //使用Service更新时间
        final Intent intent = new Intent(context, UpdateTimeService.class);
        //PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        context.startService(intent);
    }

}
