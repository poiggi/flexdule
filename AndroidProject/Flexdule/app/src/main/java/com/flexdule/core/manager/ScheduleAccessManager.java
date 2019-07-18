package com.flexdule.core.manager;

import android.content.Context;

import com.flexdule.core.dtos.Schedule;

import java.util.List;

public interface ScheduleAccessManager {

    Schedule findFirstOne() throws Exception;

    Schedule findById(Integer idSchedule) throws Exception;

    List<Schedule> findAll() throws Exception;

    long save(Schedule schedule) throws Exception;

    int deleteById(Integer idSchedule) throws Exception;
}
