package com.flexdule.android.model.sqlite;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.flexdule.android.model.sqlite.daos.ActivityDao;
import com.flexdule.android.model.sqlite.daos.CookieDao;
import com.flexdule.android.model.sqlite.daos.ScheduleDao;
import com.flexdule.android.model.sqlite.entities.ActivityBean;
import com.flexdule.android.model.sqlite.entities.CookieBean;
import com.flexdule.android.model.sqlite.entities.ScheduleBean;

@Database(version = 1, entities = {CookieBean.class, ScheduleBean.class, ActivityBean.class}, exportSchema = false)
public abstract class FlexduleDataBase extends RoomDatabase {

    abstract public CookieDao getCookieDao();
    abstract public ScheduleDao getScheduleDao();
    abstract public ActivityDao getActivityDao();
}
