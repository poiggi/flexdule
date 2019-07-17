package com.flexdule.model.sqlite.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.flexdule.model.sqlite.entities.CookieBean;

@Dao
public interface CookieDao {

    @Insert
    public long insert(CookieBean cookieBean);

    @Update
    public int update(CookieBean cookieBean);

    @Delete
    public int delete(CookieBean cookieBean);

    @Query("SELECT * FROM Cookie WHERE name = :name")
    public CookieBean findByName(String name);

}
