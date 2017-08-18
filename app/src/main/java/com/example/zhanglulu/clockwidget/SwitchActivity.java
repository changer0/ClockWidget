package com.example.zhanglulu.clockwidget;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SwitchActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private Switch mSwitchTime;
    private Switch mSwitchData;
    private Switch mSwitchWeek;
    private Switch mSwitchSetting;
    private Switch mSwitchChinese;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        initView();
    }

    private void initView() {
        setTitle("显示设置");
        mSwitchTime = (Switch) findViewById(R.id.is_display_time);
        mSwitchData = (Switch) findViewById(R.id.is_display_data);
        mSwitchWeek = (Switch) findViewById(R.id.is_display_week);
        mSwitchSetting = (Switch) findViewById(R.id.is_display_setting);
        mSwitchChinese = (Switch) findViewById(R.id.is_display_chinese);
        mSwitchTime.setOnCheckedChangeListener(this);
        mSwitchData.setOnCheckedChangeListener(this);
        mSwitchWeek.setOnCheckedChangeListener(this);
        mSwitchSetting.setOnCheckedChangeListener(this);
        mSwitchChinese.setOnCheckedChangeListener(this);
        initSwitchState();
    }

    private void initSwitchState() {
        mSwitchTime.setChecked(isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_TIME));
        mSwitchData.setChecked(isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_DATA));
        mSwitchWeek.setChecked(isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_WEEK));
        mSwitchSetting.setChecked(isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_SETTING));
        mSwitchChinese.setChecked(isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_CHINESE));
    }


    @Override
    public void onCheckedChanged(CompoundButton button, boolean b) {
        boolean isDisplay = button.isChecked();
        switch (button.getId()) {
            case R.id.is_display_time:
                saveSwitchState(ClockWidget.CLOCK_WIDGET_HAS_TIME, isDisplay);
                break;
            case R.id.is_display_data:
                saveSwitchState(ClockWidget.CLOCK_WIDGET_HAS_DATA, isDisplay);
                break;
            case R.id.is_display_week:
                saveSwitchState(ClockWidget.CLOCK_WIDGET_HAS_WEEK, isDisplay);
                break;
            case R.id.is_display_setting:
                saveSwitchState(ClockWidget.CLOCK_WIDGET_HAS_SETTING, isDisplay);
                break;
            case R.id.is_display_chinese:
                saveSwitchState(ClockWidget.CLOCK_WIDGET_HAS_CHINESE, isDisplay);
                break;
        }
    }

    private boolean isDisplayView(String key) {
        SharedPreferences sp = getSharedPreferences(ClockWidget.CLOCK_WIDGET, MODE_PRIVATE);
        return sp.getBoolean(key, true);
    }

    private void saveSwitchState(String key, boolean state) {
        SharedPreferences sp = getSharedPreferences(ClockWidget.CLOCK_WIDGET, MODE_PRIVATE);
        sp.edit().putBoolean(key, state).apply();
    }
}
