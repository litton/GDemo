package com.fantasy.coolgif.utils;

import android.util.Log;

/**
 * Created by fanlitao on 17/3/10.
 */

public  final class LogUtil {



    public static void v(String tag, String msg) {
        if(AppGlobals.DEBUG) {
            Log.v(tag,msg);
        }
    }

    public static void i(String tag, String msg) {
        if(AppGlobals.DEBUG) {
            Log.i(tag,msg);
        }
    }

    public static void e(String tag, String msg) {
        if(AppGlobals.DEBUG) {
            Log.e(tag,msg);
        }
    }
}
