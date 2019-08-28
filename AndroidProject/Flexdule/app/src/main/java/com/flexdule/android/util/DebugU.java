package com.flexdule.android.util;

import android.content.Context;
import android.util.Log;

import com.flexdule.android.manager.AndroidActivityAccessManager;
import com.flexdule.android.manager.AndroidCookieAccessManager;
import com.flexdule.android.manager.AndroidScheduleAccessManager;

import java.util.List;

import flexdule.core.dtos.Activity;
import flexdule.core.dtos.Cookie;
import flexdule.core.dtos.Schedule;
import flexdule.core.model.managers.ActivityAccessManager;
import flexdule.core.model.managers.CookieAccesManager;
import flexdule.core.model.managers.ScheduleAccessManager;

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
