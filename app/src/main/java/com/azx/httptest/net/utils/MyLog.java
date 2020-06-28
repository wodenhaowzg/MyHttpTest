package com.azx.httptest.net.utils;

import android.util.Log;

public class MyLog {

    private static final String TAG = "Network";

    public static void d(String tag, String msg){
        Log.d(TAG, tag + " - " + msg);
    }

    public static void e(String tag, String msg){
        Log.e(TAG, tag + " - " + msg);
    }

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }
}
