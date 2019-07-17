package com.flexdule.model.sqlite;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.flexdule.model.sqlite.daos.ActivityDao;
import com.flexdule.model.sqlite.daos.CookieDao;
import com.flexdule.model.sqlite.daos.ScheduleDao;
import com.flexdule.model.sqlite.entities.ActivityBean;
import com.flexdule.model.sqlite.entities.CookieBean;
import com.flexdule.model.sqlite.entities.ScheduleBean;

@Database(version = 1, entities = {CookieBean.class, ScheduleBean.class, ActivityBean.class}, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    abstract public CookieDao getCookieDao();
    abstract public ScheduleDao getScheduleDao();
    abstract public ActivityDao getActivityDao();
}
