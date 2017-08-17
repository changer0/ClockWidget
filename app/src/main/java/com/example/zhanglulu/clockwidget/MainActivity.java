package com.example.zhanglulu.clockwidget;

import android.app.MediaRouteButton;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private TextView mSeekBarBgTranVal;
    private SeekBar mSeekBarBgTran;
//    private SeekBar mSeekBarTextTran;
//    private TextView mSeekBarTextTranVal;
    private View mWidgetRootView;
    private Switch mSwitchTime;
    private Switch mSwitchData;
    private Switch mSwitchWeek;
    private Switch mSwitchSetting;
    private int mBgColor;
    private int mTextColor;
    private TextView mTimeText;
    private TextView mDataText;
    private TextView mWeekText;
    private ImageView mSettingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        setTitle("时钟插件设置");
        mSeekBarBgTran = (SeekBar) findViewById(R.id.bg_transparency_seekbar);
        mSeekBarBgTran.setMax(255);
        mSeekBarBgTranVal = (TextView) findViewById(R.id.bg_transparency_seekbar_value);
        mWidgetRootView = findViewById(R.id.widget_root);
        findViewById(R.id.widget_loading).setVisibility(View.GONE);
        mTimeText = (TextView) findViewById(R.id.widget_time);
        mTimeText.setText("12:12");
        mDataText = ((TextView) findViewById(R.id.widget_data));
        mDataText.setText("2017-08-17");
        mWeekText = ((TextView) findViewById(R.id.widget_week));
        mWeekText.setText("星期四");
        mSettingText = (ImageView) findViewById(R.id.widget_setting);
        mSwitchTime = (Switch) findViewById(R.id.is_display_time);
        mSwitchData = (Switch) findViewById(R.id.is_display_data);
        mSwitchWeek = (Switch) findViewById(R.id.is_display_week);
        mSwitchSetting = (Switch) findViewById(R.id.is_display_setting);
        mSwitchTime.setOnCheckedChangeListener(this);
        mSwitchData.setOnCheckedChangeListener(this);
        mSwitchWeek.setOnCheckedChangeListener(this);
        mSwitchSetting.setOnCheckedChangeListener(this);
        SharedPreferences sp = getSharedPreferences(ClockWidget.CLOCK_WIDGET, MODE_PRIVATE);
        setBgState(sp);
        setTextState(sp);
    }


    @Override
    public void onCheckedChanged(CompoundButton button, boolean b) {
        switch (button.getId()) {
            case R.id.is_display_time:
                saveSwitchState(ClockWidget.CLOCK_WIDGET_HAS_TIME, button.isChecked());
                break;
             case R.id.is_display_data:
                 saveSwitchState(ClockWidget.CLOCK_WIDGET_HAS_DATA, button.isChecked());
                break;
             case R.id.is_display_week:
                 saveSwitchState(ClockWidget.CLOCK_WIDGET_HAS_WEEK, button.isChecked());
                break;
             case R.id.is_display_setting:
                 saveSwitchState(ClockWidget.CLOCK_WIDGET_HAS_SETTING, button.isChecked());
                break;
        }
        setSwitchState();
    }
    private void setSwitchState() {
        if (isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_TIME)) {
            mTimeText.setVisibility(View.VISIBLE);
        } else {
            mTimeText.setVisibility(View.GONE);
        }
        if (isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_DATA)) {
            mDataText.setVisibility(View.VISIBLE);
        } else {
            mDataText.setVisibility(View.GONE);
        }
        if (isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_WEEK)) {
            mWeekText.setVisibility(View.VISIBLE);
        } else {
            mWeekText.setVisibility(View.GONE);
        }
        if (isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_SETTING)) {
            mSettingText.setVisibility(View.VISIBLE);
        } else {
            mSettingText.setVisibility(View.GONE);
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

    /**
     * 设置文本状态
     * @param sp
     */
    private void setTextState( SharedPreferences sp) {
        mTextColor = sp.getInt(ClockWidget.CLOCK_WIDGET_TEXT_COLOR, mTimeText.getCurrentTextColor());
        mTimeText.setTextColor(mTextColor);
        mDataText.setTextColor(mTextColor);
        mWeekText.setTextColor(mTextColor);

    }

    /**
     * 设置背景状态
     * @param sp
     */
    private void setBgState(SharedPreferences sp) {
        ColorDrawable colorDrawable = (ColorDrawable) mWidgetRootView.getBackground();
        mBgColor = sp.getInt(ClockWidget.CLOCK_WIDGET_BG_COLOR, colorDrawable.getColor());
        mSeekBarBgTran.setProgress(Color.alpha(mBgColor));
        mSeekBarBgTranVal.setText(String.valueOf((int)((100f/255f) * Color.alpha(mBgColor))));
        mWidgetRootView.setBackgroundColor(mBgColor);
        mSeekBarBgTran.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int alpha, boolean b) {
                int colorInt = getColorHasAlpha(alpha);
                mSeekBarBgTranVal.setText(String.valueOf((int)((100f/255f) * alpha)));
                mWidgetRootView.setBackgroundColor(colorInt);
                saveBgColor(colorInt);
            }
            @Override
            public void onStartTrackingTouch(SeekBar bar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar bar) {

            }
        });

    }

    /**
     * 返回一个设置了alpha的Color值
     * @param alpha
     * @return
     */
    private int getColorHasAlpha(int alpha) {
        int red = Color.red(mBgColor);
        int green = Color.green(mBgColor);
        int blue = Color.blue(mBgColor);
        return Color.argb(alpha, red, green, blue);
    }

    private void saveBgColor(int color) {
        SharedPreferences sp = getSharedPreferences(ClockWidget.CLOCK_WIDGET, MODE_PRIVATE);
        sp.edit().putInt(ClockWidget.CLOCK_WIDGET_BG_COLOR, color).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //启动之后，也启动一下Service
        startService(new Intent(this, UpdateTimeService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lulu", "MainActivity-onDestroy  ");
    }



    public void btnClick(View view) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intent);
        } catch (Exception e) {
            //防止crash
        }

    }

}
