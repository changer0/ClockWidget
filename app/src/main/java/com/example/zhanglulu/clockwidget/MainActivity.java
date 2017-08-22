package com.example.zhanglulu.clockwidget;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mSeekBarBgTranVal;
    private SeekBar mSeekBarBgTran;
//    private SeekBar mSeekBarTextTran;
//    private TextView mSeekBarTextTranVal;
    private View mWidgetRootView;

    private int mBgColor;
    private int mTextColor;
    private TextView mTimeText;
    private TextView mDataText;
    private TextView mWeekText;
    private ImageView mSettingText;
    private View mMiuiSetting;
    private TextView mChineseText;

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
        mChineseText = (TextView) findViewById(R.id.widget_chinese);
        mChineseText.setText("农历 六月廿六");

        mMiuiSetting = findViewById(R.id.miui_setting);
        mSettingText = (ImageView) findViewById(R.id.widget_setting);
        findViewById(R.id.switch_setting).setOnClickListener(this);
        findViewById(R.id.text_size_setting).setOnClickListener(this);
        mMiuiSetting.setOnClickListener(this);
        SharedPreferences sp = getSharedPreferences(ClockWidget.CLOCK_WIDGET, MODE_PRIVATE);
        setBgState(sp);
        setTextState(sp);
    }


    /**
     * 重新刷新一下View
     */
    private void refreshView() {
        mTimeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_TIME, this));
        mDataText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_DATE, this));
        mWeekText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_WEEK, this));
        mChineseText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_CHINESE, this));

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
        if (isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_CHINESE)) {
            mChineseText.setVisibility(View.VISIBLE);
        } else {
            mChineseText.setVisibility(View.GONE);
        }
        if (isDisplayView(ClockWidget.CLOCK_WIDGET_HAS_SECOND)) {
            mTimeText.setText("12:12:12");
        } else {
            mTimeText.setText("12:12");
        }

    }

    private boolean isDisplayView(String key) {
        SharedPreferences sp = getSharedPreferences(ClockWidget.CLOCK_WIDGET, MODE_PRIVATE);
        return sp.getBoolean(key, true);
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
        refreshView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lulu", "MainActivity-onDestroy  ");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.miui_setting:
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                    startActivity(intent);
                } catch (Exception e) {
                    //防止crash
                }
                break;
            case R.id.switch_setting:
                startActivity(new Intent(this, SwitchActivity.class));
                break;
            case R.id.text_size_setting:
                startActivity(new Intent(this, TextSizeSettingActivity.class));
                break;
        }
    }
}
