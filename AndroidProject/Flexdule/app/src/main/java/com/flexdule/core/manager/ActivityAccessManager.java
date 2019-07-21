package com.flexdule.core.manager;

import com.flexdule.core.dtos.Activity;

import java.util.List;

public interface ActivityAccessManager {


    List<Activity> findActivitiesBySchedule(Integer idSchedule) throws Exception;

    List<Activity> findAllActivities() throws Exception;

    long saveActivity(Activity activity) throws Exception;

    List<Long> saveActivities(List<Activity> activities) throws Exception;

    int deleteActivityById(Integer idActivity) throws Exception;
}
