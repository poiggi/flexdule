package com.flexdule.android.model.sqlite.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.flexdule.android.model.sqlite.entities.ActivityBean;

import java.util.List;

@Dao
public interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long save(ActivityBean activityBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public List<Long> save(List<ActivityBean> activityBeans);

    @Update
    public int update(ActivityBean activityBean);

    @Query("DELETE FROM Activity WHERE idActivity = :idActivity")
    public int deleteById(Integer idActivity);

    @Query("SELECT * FROM Activity WHERE idActivity = :idActivity")
    public ActivityBean findById(Integer idActivity);

    @Query("SELECT * FROM Activity WHERE Schedule_idSchedule = :idSchedule order by Schedule_idSchedule")
    public List<ActivityBean> findBySchedule(Integer idSchedule);

    @Query("SELECT * FROM Activity")
    public List<ActivityBean> findAll();
}
