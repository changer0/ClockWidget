package com.example.zhanglulu.clockwidget;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class TextSizeSettingActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private SeekBar mTimeSeekBar;
    private SeekBar mDateSeekBar;
    private SeekBar mWeekSeekBar;
    private SeekBar mChineseSeekBar;

    private TextView mTimeVal;
    private TextView mDateVal;
    private TextView mWeekVal;
    private TextView mChineseVal;
    private TextView mDisplayTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_size_setting);
        initView();
        initData();
    }

    private void initView() {
        mTimeSeekBar = (SeekBar) findViewById(R.id.time_size_seekbar);
        mDateSeekBar = (SeekBar) findViewById(R.id.date_size_seekbar);
        mWeekSeekBar = (SeekBar) findViewById(R.id.week_size_seekbar);
        mChineseSeekBar = (SeekBar) findViewById(R.id.chinese_size_seekbar);

        mTimeVal = (TextView) findViewById(R.id.time_size_seekbar_val);
        mDateVal = (TextView) findViewById(R.id.date_size_seekbar_val);
        mWeekVal = (TextView) findViewById(R.id.week_size_seekbar_val);
        mChineseVal = (TextView) findViewById(R.id.chinese_size_seekbar_val);
        mDisplayTv = (TextView) findViewById(R.id.text_size_display);

        mTimeSeekBar.setOnSeekBarChangeListener(this);
        mDateSeekBar.setOnSeekBarChangeListener(this);
        mWeekSeekBar.setOnSeekBarChangeListener(this);
        mChineseSeekBar.setOnSeekBarChangeListener(this);

        findViewById(R.id.text_size_reset).setOnClickListener(this);

        mTimeSeekBar.setMax(getResources().getDimensionPixelOffset(R.dimen.max_size));
        mDateSeekBar.setMax(getResources().getDimensionPixelOffset(R.dimen.max_size));
        mWeekSeekBar.setMax(getResources().getDimensionPixelOffset(R.dimen.max_size));
        mChineseSeekBar.setMax(getResources().getDimensionPixelOffset(R.dimen.max_size));
    }

    private void initData() {
        setSeekVal(mTimeSeekBar, mTimeVal, Utils.getTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_TIME, this));
        setSeekVal(mDateSeekBar, mDateVal, Utils.getTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_DATE, this));
        setSeekVal(mWeekSeekBar, mWeekVal, Utils.getTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_WEEK, this));
        setSeekVal(mChineseSeekBar, mChineseVal, Utils.getTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_CHINESE, this));
    }

    private void setSeekVal(SeekBar seekbar, TextView val, int size) {
        seekbar.setProgress(size);
        val.setText(String.valueOf(size));
    }



    //---------------------------------
    //SeekBar监听回调
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }
        switch (seekBar.getId()) {
            case R.id.time_size_seekbar:
                setSeekVal(mTimeSeekBar, mTimeVal, progress);
                Utils.saveTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_TIME, progress, this);
                break;
            case R.id.date_size_seekbar:
                setSeekVal(mDateSeekBar, mDateVal, progress);
                Utils.saveTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_DATE, progress, this);
                break;
            case R.id.week_size_seekbar:
                setSeekVal(mWeekSeekBar, mWeekVal, progress);
                Utils.saveTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_WEEK, progress, this);
                break;
            case R.id.chinese_size_seekbar:
                setSeekVal(mChineseSeekBar, mChineseVal, progress);
                Utils.saveTextSize(ClockWidget.CLOCK_WIDGET_TEXT_SIZE_CHINESE, progress, this);
                break;
        }
        mDisplayTv.setTextSize(0, progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_size_reset:
                Utils.resetTextSize(this);
                initData();
                break;
        }
    }
}
