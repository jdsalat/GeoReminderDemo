package com.georeminder.src.utils;

import android.support.design.BuildConfig;
import android.util.Log;

/**
 * Created by DELL on 3/17/2016.
 */
public class ClientLogs {

    public static String debugLogType = "debug";
    public static String infoLogType = "info";
    public static String errorLogType = "error";
    private static boolean isLogNeeded = true;

    public static void printLogs(String logType, String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            if (isLogNeeded) {
                if (logType.equalsIgnoreCase(debugLogType)) {
                    Log.d(TAG, msg);
                } else if (logType.equalsIgnoreCase(infoLogType)) {
                    Log.i(TAG, msg);
                } else if (logType.equalsIgnoreCase(errorLogType)) {
                    Log.e(TAG, msg);
                }
            }
        }
    }
}
