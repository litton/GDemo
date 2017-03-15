package com.fantasy.coolgif.utils;

import com.fantasy.coolgif.main.MainApplication;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by fanlitao on 17/3/15.
 */

public class AnalyticsEvent {


    public static void onEvent(String eventId) {
        MobclickAgent.onEvent(MainApplication.getInstance().getApplicationContext(),eventId);
    }
}
