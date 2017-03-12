package com.fantasy.coolgif.main;

import android.app.Application;

/**
 * Created by fanlitao on 17/3/9.
 */

public class MainApplication extends Application {

    private static MainApplication sInstance;

    public static MainApplication getInstance() {
        return sInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
