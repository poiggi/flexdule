package com.flexdule.core.manager;

import com.flexdule.core.dtos.Activity;

import java.util.List;

public interface ActivityAccessManager {

    public List<Activity> findBySchedule(Integer idSchedule) throws Exception;

    List<Activity> findAll() throws Exception;

    public long save(Activity activity) throws Exception;

    public List<Long> save(List<Activity> activities) throws Exception;

    public int deleteById(Integer idActivity) throws Exception;






}
