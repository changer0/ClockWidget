package com.example.zhanglulu.clockwidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.zhanglulu.clockwidget.chinesecalendar.Lunar;
import com.example.zhanglulu.clockwidget.chinesecalendar.LunarSolarConverter;
import com.example.zhanglulu.clockwidget.chinesecalendar.Solar;
import com.example.zhanglulu.clockwidget.chinesecalendar.Util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhanglulu on 2017/8/16.
 */

public class UpdateTimeService extends Service {


    private Timer mTimer;

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
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

//    class UpdateTimeServiceBind extends Binder{
//        public UpdateTimeService getUpdateTimeService () {
//            return UpdateTimeService.this;
//        }
//    }

    private void updateWidget(Context context){


        RemoteViews updateView = new RemoteViews(context.getPackageName(),
                R.layout.time_widget_layout);
        updateData(updateView);
        updateState(updateView);
        //点击事件启动闹钟
        //Intent launchAlarms = new Intent(AlarmClock.ACTION_SET_ALARM);
        //updateView.setOnClickPendingIntent(R.id.widget_time, PendingIntent.getActivity(context, 0, launchAlarms, 0));
        //跳转设置页面
        //Intent settingIntent = new Intent();
        //ComponentName comp = new ComponentName("com.android.settings",
        //"com.android.settings.Settings");
        //settingIntent.setComponent(comp);
        //settingIntent.setAction("android.intent.action.VIEW");


        String clockPackageName = "com.android.deskclock";
        String clockActivityName = "";
        String calendarPackageName = "com.android.calendar";
        String calendarActivityName = "";

        if (Utils.isHasActivity("com.android.deskclock.AlarmsMainActivity",clockPackageName, this)) {
            clockActivityName = "com.android.deskclock.AlarmsMainActivity";
        } else if (Utils.isHasActivity("com.oneplus.deskclock.DeskClock", "com.oneplus.deskclock", this)) {
            clockPackageName = "com.oneplus.deskclock";
            clockActivityName = "com.oneplus.deskclock.DeskClock";
        } else {
            clockActivityName = "com.android.alarmclock.AlarmClock";
        }
        Intent alarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(
                Intent.CATEGORY_LAUNCHER).setComponent(
                new ComponentName(clockPackageName, clockActivityName));
        updateView.setOnClickPendingIntent(R.id.widget_time, PendingIntent.getActivity(context, 0, alarmClockIntent, 0));


        if (Utils.isHasActivity("com.android.calendar.AllInOneActivity", calendarPackageName, this)) {
            calendarActivityName = "com.android.calendar.AllInOneActivity";
        } else if(Utils.isHasActivity("com.android.calendar.AllInOneActivity", "com.oneplus.calendar", this)){
            calendarActivityName = "com.android.calendar.AllInOneActivity";
            calendarPackageName = "com.oneplus.calendar";
        } else {
            calendarActivityName = "com.android.calendar.homepage.AllInOneActivity";
        }

        //点击widget，启动日历
        Intent launchCalendar = new Intent();
        launchCalendar.setComponent(new ComponentName(calendarPackageName,
                calendarActivityName));
        launchCalendar.setAction(Intent.ACTION_MAIN);
        launchCalendar.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingCalendarIntent = PendingIntent.getActivity(context, 0,
                launchCalendar, 0);
        updateView.setOnClickPendingIntent(R.id.widget_data, pendingCalendarIntent);
        updateView.setOnClickPendingIntent(R.id.widget_week, pendingCalendarIntent);
        updateView.setOnClickPendingIntent(R.id.widget_chinese, pendingCalendarIntent);


        //跳转设置页面
        Intent launchSetting = new Intent();
        launchSetting.setClass(this, MainActivity.class);

        PendingIntent pendingSettingIntent = PendingIntent.getActivity(context, 0,
                launchSetting, 0);
        updateView.setOnClickPendingIntent(R.id.widget_setting, pendingSettingIntent);
        //刷新Widget
        AppWidgetManager awg = AppWidgetManager.getInstance(context);
        awg.updateAppWidget(new ComponentName(context, ClockWidget.class),
                updateView);
    }

    /**
     * 更新数据
     * @param updateView
     */
    private void updateData(RemoteViews updateView) {
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
        updateView.setTextViewText(R.id.widget_time, strTime);
        updateView.setTextViewText(R.id.widget_data, strData);
        updateView.setTextViewText(R.id.widget_week, getWeekString(time.weekDay));

        Lunar lunar = LunarSolarConverter.SolarToLunar(new Solar(day, month, year));
        updateView.setTextViewText(R.id.widget_chinese, "农历 "+ Util.getLunarNameOfMonth(lunar.lunarMonth)
                + "月"  + Util.getLunarNameOfDay(lunar.lunarDay));
        updateView.setViewVisibility(R.id.widget_loading, View.GONE);
        Log.d("lulu", "UpdateTimeService-updateWidget time => " + strTime);
    }


    /**
     * 更新控件状态
     * @param view
     */
    private void updateState(RemoteViews view) {
        view.setInt(R.id.widget_root, "setBackgroundColor", getBgColor());
        if (isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_TIME)) {
            view.setViewVisibility(R.id.widget_time, View.VISIBLE);
        } else {
            view.setViewVisibility(R.id.widget_time, View.GONE);
        }
        if (isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_DATA)) {
            view.setViewVisibility(R.id.widget_data, View.VISIBLE);
        } else {
            view.setViewVisibility(R.id.widget_data, View.GONE);
        }
        if (isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_WEEK)) {
            view.setViewVisibility(R.id.widget_week, View.VISIBLE);
        } else {
            view.setViewVisibility(R.id.widget_week, View.GONE);
        }
        if (isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_SETTING)) {
            view.setViewVisibility(R.id.widget_setting, View.VISIBLE);
        } else {
            view.setViewVisibility(R.id.widget_setting, View.GONE);
        }
        if (isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_CHINESE)) {
            view.setViewVisibility(R.id.widget_chinese, View.VISIBLE);
        } else {
            view.setViewVisibility(R.id.widget_chinese, View.GONE);
        }
        view.setTextViewTextSize(R.id.widget_time, 0, Utils.getTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_TIME, this));
        view.setTextViewTextSize(R.id.widget_data, 0, Utils.getTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_DATE, this));
        view.setTextViewTextSize(R.id.widget_week, 0, Utils.getTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_WEEK, this));
        view.setTextViewTextSize(R.id.widget_chinese, 0, Utils.getTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_CHINESE, this));


    }


    private int getBgColor() {
        SharedPreferences sp = getSharedPreferences(ClockWidget.CLOCK_WIDGET, MODE_PRIVATE);
        return sp.getInt(ClockWidget.CLOCK_WIDGET_BG_COLOR, getResources().getColor(R.color.widget_bg));
    }

    private boolean isDisplayView(String key) {
        SharedPreferences sp = getSharedPreferences(ClockWidget.CLOCK_WIDGET, MODE_PRIVATE);
        return sp.getBoolean(key, true);
    }

    private String getWeekString(int week) {
        String ret = "星期";
        String[] weekStr = new String[]{"日","一","二","三","四","五","六"};
        return ret + weekStr[week];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTimer = null;
        Log.d("lulu", "UpdateTimeService-onDestroy  " );
    }
}
