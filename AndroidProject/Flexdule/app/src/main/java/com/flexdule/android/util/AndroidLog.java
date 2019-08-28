package com.flexdule.android.util;

import android.util.Log;

import flexdule.core.util.CoreLog;

public class AndroidLog implements CoreLog {

    String tag;

    public AndroidLog(String tag) {
        this.tag = tag;
    }

    @Override
    public void d(String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void i(String msg) {
        Log.i(tag, msg);
    }

    @Override
    public void w(String msg) {
        Log.w(tag, msg);
    }

    @Override
    public void e(String msg) {
        Log.e(tag, msg);
    }
}
