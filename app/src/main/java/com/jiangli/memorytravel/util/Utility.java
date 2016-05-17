package com.jiangli.memorytravel.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.jiangli.memorytravel.R;

/**
 * Created by Administrator on 2016/5/17 0017.
 */
public class Utility {
    public static void vibrate(Context context, int i) {
//        Context context = this;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean aBoolean = prefs.getBoolean(context.getString(R.string.pref_enable_vibrate_key),
                Boolean.parseBoolean(context.getString(R.string.pref_enable_vibrate_default)));
        if (aBoolean) {
            final Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            long [] pattern = {100, i};   // 停止 开启 停止 开启
            vibrator.vibrate(pattern,-1);
        }
    }
    public static boolean getPreferenceBoolean(Preference preference, int defaultId) {
        return PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getBoolean(preference.getKey(), Boolean.parseBoolean(preference.getContext().getString(defaultId)));

    }
    public static String getPreferenceString(Preference preference, int defaultId) {
        return  PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), preference.getContext().getString(defaultId));

    }
}
