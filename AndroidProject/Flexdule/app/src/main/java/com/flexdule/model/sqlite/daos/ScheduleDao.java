package com.flexdule.model.sqlite.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.flexdule.model.sqlite.entities.CookieBean;
import com.flexdule.model.sqlite.entities.ScheduleBean;

@Dao
public interface ScheduleDao {

    @Insert
    public long insert(ScheduleBean scheduleBean);

    @Update
    public int update(ScheduleBean scheduleBean);

    @Delete
    public int delete(ScheduleBean scheduleBean);

    @Query("SELECT * FROM Schedule WHERE idSchedule = :idSchedule")
    public ScheduleBean findById(Integer idSchedule);

}