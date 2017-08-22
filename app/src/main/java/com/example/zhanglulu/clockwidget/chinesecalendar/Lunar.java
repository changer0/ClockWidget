package com.example.zhanglulu.clockwidget.chinesecalendar;

/**
 * Created by lulu on 2017/8/18.
 * 农历
 */

public class Lunar {
    public boolean isleap;
    public int lunarDay;
    public int lunarMonth;
    public int lunarYear;

    public Lunar() {
    }

    public Lunar(boolean isleap, int lunarDay, int lunarMonth, int lunarYear) {
        this.isleap = isleap;
        this.lunarDay = lunarDay;
        this.lunarMonth = lunarMonth;
        this.lunarYear = lunarYear;
    }
}
