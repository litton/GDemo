package com.fantasy.coolgif.utils;

import android.content.Context;
import android.os.Looper;
import android.telephony.TelephonyManager;

import com.fantasy.coolgif.main.MainApplication;

/**
 * Created by fanlitao on 17/3/9.
 */

public class Utils {

    public static String[]IMEIS = new String[]{"353609072179246"};

    public static boolean isMainThread() {
        return  Looper.getMainLooper() == Looper.myLooper();
    }


    public static boolean isSuperAccount() {
        LogUtil.v("fan","getIMEI:" + Utils.getIMEI());
        final String IMEI = getIMEI();
        boolean isSuperAccount = false;
        for (int i =0;i < IMEIS.length;i++) {
            if(IMEIS[i].equals(IMEI)) {
                isSuperAccount = true;
            }
        }
        return isSuperAccount;
    }

    public static String getIMEI() {
        Context context = MainApplication.getInstance().getApplicationContext();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

}
