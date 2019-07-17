package com.flexdule.model.sqlite.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.flexdule.model.sqlite.entities.ActivityBean;
import com.flexdule.model.sqlite.entities.ScheduleBean;

@Dao
public interface ActivityDao {

    @Insert
    public long insert(ActivityBean activityBean);

    @Update
    public int update(ActivityBean activityBean);

    @Delete
    public int delete(ActivityBean activityBean);

    @Query("SELECT * FROM Activity WHERE idActivity = :idActivity")
    public ActivityBean findById(Integer idActivity);
}
