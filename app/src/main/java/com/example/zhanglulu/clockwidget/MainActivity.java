package com.example.zhanglulu.clockwidget;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mSeekBarBgTranVal;
    private SeekBar mSeekBarBgTran;
    private SeekBar mSeekBarTextTran;
    private View mWidgetRootView;
    private Switch mSwitchTime;
    private int mBgColor;
    private int mTextColor;
    private TextView mTimeText;
    private TextView mDataText;
    private TextView mWeekText;
    private TextView mSeekBarTextTranVal;

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
        mSeekBarTextTran = (SeekBar) findViewById(R.id.text_transparency_seekbar);
        mSeekBarTextTran.setMax(255);
        mSeekBarTextTranVal = ((TextView) findViewById(R.id.text_transparency_seekbar_value));
        mWidgetRootView = findViewById(R.id.widget_root);
        findViewById(R.id.widget_loading).setVisibility(View.GONE);
        mTimeText = (TextView) findViewById(R.id.widget_time);
        mTimeText.setText("12:12");
        mDataText = ((TextView) findViewById(R.id.widget_data));
        mDataText.setText("2017-08-17");
        mWeekText = ((TextView) findViewById(R.id.widget_week));
        mWeekText.setText("星期四");
        mSwitchTime = (Switch) findViewById(R.id.is_display_time);
        SharedPreferences sp = getSharedPreferences(ClockWidget.CLOCK_WIDGET, MODE_PRIVATE);
        setBgState(sp);
        setTextState(sp);
    }

    /**
     * 设置文本状态
     * @param sp
     */
    private void setTextState( SharedPreferences sp) {
        mTextColor = sp.getInt(ClockWidget.CLOCK_WIDGET_TEXT_COLOR, mTimeText.getCurrentTextColor());
        mSeekBarTextTran.setProgress(Color.alpha(mTextColor));
        mTimeText.setTextColor(mTextColor);
        mDataText.setTextColor(mTextColor);
        mWeekText.setTextColor(mTextColor);
        mSeekBarTextTran.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int alpha, boolean b) {
                int colorInt = getColorHasAlpha(alpha);
                mSeekBarTextTranVal.setText(String.valueOf((int)((100f/255f) * alpha)));
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
