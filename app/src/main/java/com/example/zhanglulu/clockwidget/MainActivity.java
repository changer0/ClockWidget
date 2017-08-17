package com.example.zhanglulu.clockwidget;

import android.app.ActionBar;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mSeekBarTranVal;
    private SeekBar mSeekBarBgTran;
    private View mWidgetRootView;
    private View mSwitchTime;
    private UpdateTimeService mUpdateTimeService;
    private boolean isBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindUpdateTimeService();
        initView();

        mSeekBarBgTran.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int i, boolean b) {
                Drawable background = mWidgetRootView.getBackground();
                int color = Color.TRANSPARENT;
                if (background instanceof ColorDrawable) {
                    ColorDrawable colorDrawable = (ColorDrawable) background;
                    colorDrawable.setAlpha(i);
                    mWidgetRootView.setBackground(colorDrawable);
                    mSeekBarTranVal.setText(String.valueOf((int)((100f/255f) * i)));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar bar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar bar) {

            }
        });
    }

    private void initView() {
        setTitle("时钟插件设置");
        mSeekBarBgTran = (SeekBar) findViewById(R.id.bg_transparency_seekbar);
        mSeekBarBgTran.setMax(255);
        mSeekBarTranVal = (TextView) findViewById(R.id.bg_transparency_seekbar_value);
        mWidgetRootView = findViewById(R.id.widget_root);
        mSwitchTime = findViewById(R.id.is_display_time);

        Drawable background = mWidgetRootView.getBackground();

        if (background instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) background;
            int alpha = colorDrawable.getAlpha();
            mSeekBarTranVal.setText(String.valueOf((int)((100f/255f) * alpha)));
            mSeekBarBgTran.setProgress(alpha);
            mUpdateTimeService.setRootBgColor(colorDrawable.getColor());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //启动之后，也启动一下Service
        startService(new Intent(this, UpdateTimeService.class));
    }

    @Override
    protected void onDestroy() {
        unBindUpdateTimeService();
        super.onDestroy();
        Log.d("lulu", "MainActivity-onDestroy  ");
    }


    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            UpdateTimeService.UpdateTimeServiceBind timeServiceBind = (UpdateTimeService.UpdateTimeServiceBind) binder;
            mUpdateTimeService = timeServiceBind.getUpdateTimeService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private void bindUpdateTimeService() {
        if (!isBound) {
            bindService(new Intent(this, UpdateTimeService.class),conn, Context.BIND_AUTO_CREATE);
            isBound = true;
        }
    }

    private void unBindUpdateTimeService() {
        if (isBound) {
            unbindService(conn);
            isBound = false;
        }
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
