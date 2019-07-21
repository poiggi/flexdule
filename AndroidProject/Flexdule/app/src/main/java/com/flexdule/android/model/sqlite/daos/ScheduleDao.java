package com.flexdule.android.model.sqlite.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.flexdule.android.model.sqlite.entities.ScheduleBean;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Query("SELECT * FROM Schedule ORDER BY ROWID ASC LIMIT 1")
    public ScheduleBean findFirstOne();

    @Query("SELECT * FROM Schedule WHERE idSchedule = :idSchedule")
    public ScheduleBean findById(Integer idSchedule);

    @Query("SELECT * FROM Schedule")
    public List<ScheduleBean> findAll();

    @Insert
    public long insert(ScheduleBean scheduleBean);

    @Update
    public int update(ScheduleBean scheduleBean);

    @Query("DELETE FROM Schedule WHERE idSchedule = :idSchedule")
    public int deleteById(Integer idSchedule);


}