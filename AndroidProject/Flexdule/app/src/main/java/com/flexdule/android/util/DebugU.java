package com.flexdule.android.util;

import android.content.Context;
import android.util.Log;

import com.flexdule.android.manager.AndroidActivityAccessManager;
import com.flexdule.android.manager.AndroidCookieAccessManager;
import com.flexdule.android.manager.AndroidScheduleAccessManager;
import com.flexdule.core.dtos.Activity;
import com.flexdule.core.dtos.Cookie;
import com.flexdule.core.dtos.Schedule;
import com.flexdule.core.manager.ActivityAccessManager;
import com.flexdule.core.manager.CookieAccesManager;
import com.flexdule.core.manager.ScheduleAccessManager;

import java.util.List;

public class DebugU {
    private static final String tag = DebugU.class.getName();

    public static void printAllPersistentActivities(Context c) throws Exception {
        ActivityAccessManager am = new AndroidActivityAccessManager(c);
        List<Activity> list = am.findAllActivities();
        for (Activity item : list) {
            Log.i(tag, item.toString());
        }
    }

    public static void printAllPersistentSchedules(Context c) throws Exception {

        ScheduleAccessManager am = new AndroidScheduleAccessManager(c);
        List<Schedule> list = am.findAllSchedules();
        for (Schedule item : list) {
            Log.i(tag, item.toString());
        }

    }

    public static void printAllPersistentCookies(Context c) throws Exception {
        CookieAccesManager am = new AndroidCookieAccessManager(c);
        List<Cookie> list = am.findAllCookies();
        for (Cookie item : list) {
            Log.i(tag, item.toString());
        }
    }


    public static void printAllDb(Context c) throws Exception {
        DebugU.printAllPersistentCookies(c);
        DebugU.printAllPersistentSchedules(c);
        DebugU.printAllPersistentActivities(c);
    }


}
