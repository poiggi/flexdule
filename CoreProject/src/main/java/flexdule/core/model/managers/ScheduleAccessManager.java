package flexdule.core.model.managers;

import java.util.List;

import flexdule.core.dtos.Schedule;

public interface ScheduleAccessManager {


    Schedule findFirstSchedule() throws Exception;

    Schedule findScheduleById(Integer idSchedule) throws Exception;

    List<Schedule> findAllSchedules() throws Exception;

    Integer saveSchedule(Schedule schedule) throws Exception;

    int deleteScheduleById(Integer idSchedule) throws Exception;
}
