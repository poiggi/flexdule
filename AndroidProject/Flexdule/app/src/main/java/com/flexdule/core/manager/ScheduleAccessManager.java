package com.flexdule.core.manager;

import com.flexdule.core.dtos.Schedule;

import java.util.List;

public interface ScheduleAccessManager {


    Schedule findFirstSchedule() throws Exception;

    Schedule findScheduleById(Integer idSchedule) throws Exception;

    List<Schedule> findAllSchedules() throws Exception;

    Integer saveSchedule(Schedule schedule) throws Exception;

    int deleteScheduleById(Integer idSchedule) throws Exception;
}
