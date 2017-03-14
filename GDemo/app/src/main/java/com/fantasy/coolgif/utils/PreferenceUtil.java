/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fantasy.coolgif.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.fantasy.coolgif.main.MainApplication;


/**
 * Created by fanlitao on 16/7/26.
 * <p/>
 */
public final class PreferenceUtil {

    public static volatile String gDeviceUUID = null;
    private static boolean sSaveDeviceuuid = true;
    private static SharedPreferences mMainSharedPreference;
    private static final String MAIN_PREFERENCE_FILE_NAME = "coolgif_prefs";
    public static final String GIF_PLAY_POSITION = "gif_play_position";


    public static SharedPreferences getSharedPreferences() {
        if (mMainSharedPreference == null) {
            Context context = MainApplication.getInstance();
            mMainSharedPreference = context.getSharedPreferences(MAIN_PREFERENCE_FILE_NAME, Context.MODE_MULTI_PROCESS);
        }
        return mMainSharedPreference;
    }


    public static void saveGifCurrentPos(int pos) {
        getSharedPreferences();
        mMainSharedPreference.edit().putInt(GIF_PLAY_POSITION, pos).commit();
    }

    public static int getGifRequestPosition() {
        getSharedPreferences();
        return mMainSharedPreference.getInt(GIF_PLAY_POSITION, 0);
    }


}
