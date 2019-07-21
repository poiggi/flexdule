package com.flexdule.android.model.sqlite.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.flexdule.android.model.sqlite.entities.CookieBean;
import com.flexdule.android.model.sqlite.entities.ScheduleBean;

import java.util.List;

@Dao
public interface CookieDao {

    @Query("SELECT * FROM Cookie WHERE name = :name")
    public CookieBean findByName(String name);

    @Query("SELECT * FROM Cookie")
    public List<CookieBean> findAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long save(CookieBean cookieBean);

    @Query("DELETE FROM Cookie WHERE name = :name")
    public int deleteByName(String name);


}
