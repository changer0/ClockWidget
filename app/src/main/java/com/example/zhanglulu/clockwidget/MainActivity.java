package com.example.zhanglulu.clockwidget;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
