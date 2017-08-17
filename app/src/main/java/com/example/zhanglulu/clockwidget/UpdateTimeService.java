package com.example.zhanglulu.clockwidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.AlarmClock;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhanglulu on 2017/8/16.
 */

public class UpdateTimeService extends Service {

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateWidget(UpdateTimeService.this);
            }
        }, 1000, 1000);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void updateWidget(Context context){
        //不用Calendar，Time对cpu负荷较小
        Time time = new Time();
        time.setToNow();
        int hour = time.hour;
        int min = time.minute;
        int second = time.second;
        int year = time.year;
        int month = time.month+1;
        int day = time.monthDay;

        String strTime = String.format("%02d:%02d", hour, min);
        String strData = String.format("%04d-%02d-%02d", year, month, day);
        RemoteViews updateView = new RemoteViews(context.getPackageName(),
                R.layout.time_widget_layout);
        updateView.setTextViewText(R.id.widget_time, strTime);
        updateView.setTextViewText(R.id.widget_data, strData);
        updateView.setTextViewText(R.id.widget_week, getWeekString(time.weekDay));
        updateView.setViewVisibility(R.id.widget_loading, View.GONE);

        Log.d("lulu", "UpdateTimeService-updateWidget time => " + strTime);
        //点击事件启动闹钟
        //Intent launchAlarms = new Intent(AlarmClock.ACTION_SET_ALARM);
        //updateView.setOnClickPendingIntent(R.id.widget_time, PendingIntent.getActivity(context, 0, launchAlarms, 0));
        //跳转设置页面
        //Intent settingIntent = new Intent();
        //ComponentName comp = new ComponentName("com.android.settings",
        //"com.android.settings.Settings");
        //settingIntent.setComponent(comp);
        //settingIntent.setAction("android.intent.action.VIEW");



        Intent AlarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(
                Intent.CATEGORY_LAUNCHER).setComponent(
                new ComponentName("com.android.deskclock", "com.android.alarmclock.AlarmClock"));
        updateView.setOnClickPendingIntent(R.id.widget_time, PendingIntent.getActivity(context, 0, AlarmClockIntent, 0));


        //点击widget，启动日历
        Intent launchCalendar = new Intent();
        launchCalendar.setComponent(new ComponentName("com.android.calendar",
                "com.android.calendar.homepage.AllInOneActivity"));
        launchCalendar.setAction(Intent.ACTION_MAIN);
        launchCalendar.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingCalendarIntent = PendingIntent.getActivity(context, 0,
                launchCalendar, 0);
        updateView.setOnClickPendingIntent(R.id.widget_data, pendingCalendarIntent);
        updateView.setOnClickPendingIntent(R.id.widget_week, pendingCalendarIntent);

        //刷新Widget
        AppWidgetManager awg = AppWidgetManager.getInstance(context);
        awg.updateAppWidget(new ComponentName(context, ClockWidget.class),
                updateView);
    }


    private String getWeekString(int week) {
        String ret = "星期";
        String[] weekStr = new String[]{"日","一","二","三","四","五","六"};
        return ret + weekStr[week];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("lulu", "UpdateTimeService-onDestroy  " );
    }
}
