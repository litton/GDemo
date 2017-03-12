package com.fantasy.coolgif.utils;

import android.os.Looper;

/**
 * Created by fanlitao on 17/3/9.
 */

public class Utils {


    public static boolean isMainThread() {
        return  Looper.getMainLooper() == Looper.myLooper();
    }
}
