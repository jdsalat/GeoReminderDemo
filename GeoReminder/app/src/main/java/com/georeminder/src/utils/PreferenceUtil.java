package com.georeminder.src.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Javed.Salat on 9/17/2016.
 */
public class PreferenceUtil {
    private static final String TAG = PreferenceUtil.class.getSimpleName();
    private static final String PREF_NAME = "pref_name";
    private Context mContext;
    private static PreferenceUtil preferenceUtil = null;
    public static final String IS_SOUND_ON = "is_sound_on";
    public static final String IS_VIBRATE_ON = "is_vibrate_on";

    public PreferenceUtil(Context mContext) {
        this.mContext = mContext;
    }

    public static PreferenceUtil newInstance(Context context) {
        if (preferenceUtil == null) {
            preferenceUtil = new PreferenceUtil(context);
        }
        return preferenceUtil;
    }

    public SharedPreferences getSharedPreference() {
        return mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getSharedPreferenceEditor() {
        return mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
    }
}
