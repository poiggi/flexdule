package com.flexdule.core.dtos;

import java.io.Serializable;
import java.util.List;

public class ScheduleWithActivities implements Serializable {
    private static final long serialVersionUID = 1L;

    private Schedule schedule;
    private List<Activity> activties;

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public List<Activity> getActivties() {
        return activties;
    }

    public void setActivties(List<Activity> activties) {
        this.activties = activties;
    }

    @Override
    public String toString() {
        String s = "ScheduleWithActivities [ schedule=" + schedule + ", activties= ";
        for (Activity ac : activties) {
            s += "\n\t" + ac.toString();
        }
        s += "]\n";
        return s;
    }

}
