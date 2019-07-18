package com.flexdule.core.manager;

import com.flexdule.core.dtos.Activity;

import java.util.ArrayList;

public class ScheduleActivitiesManager {

    private ArrayList<Activity> activities;
    private ActivityAccessManager accesManager;

    public ScheduleActivitiesManager(ActivityAccessManager accesManager, ArrayList<Activity> activities){
        this.activities = activities;
        this.accesManager = accesManager;
    }

    public void calculateFinalDurs(){
        calculateIndividualFinalDurs();
        calculateScheduleFinalDurs();
    }
    
    public void calculateIndividualFinalDurs(){

    }

    public void calculateScheduleFinalDurs(){

    }
}
