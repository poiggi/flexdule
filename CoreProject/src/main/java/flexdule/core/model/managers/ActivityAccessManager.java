package flexdule.core.model.managers;


import java.util.List;

import flexdule.core.dtos.Activity;

public interface ActivityAccessManager {


    List<Activity> findActivitiesBySchedule(Integer idSchedule) throws Exception;

    List<Activity> findAllActivities() throws Exception;

    long saveActivity(Activity activity) throws Exception;

    List<Long> saveActivities(List<Activity> activities) throws Exception;

    int deleteActivityById(Integer idActivity) throws Exception;
}
