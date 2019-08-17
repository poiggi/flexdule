package com.flexdule;

import android.support.test.runner.AndroidJUnit4;

import com.flexdule.android.util.AndroidLog;
import com.flexdule.core.dtos.Activity;
import com.flexdule.core.dtos.ActivityVars;
import com.flexdule.core.manager.ScheduleActivitiesManager;
import com.flexdule.core.util.CoreLog;
import com.flexdule.core.util.Time;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ScheduleActivitiesManagerTest {

    @Test
    public void calcConfigToFinal() {
        CoreLog log = new AndroidLog("calcConfigToFinal");
        ScheduleActivitiesManager manager = new ScheduleActivitiesManager(log);

        Activity ac = new Activity();
        ActivityVars conf = ac.getConfigVars();
        ActivityVars fin = ac.getFinalVars();

        log.i("0: "+ac);

        conf.setSn(new Time("8:00"));
        conf.setSx(new Time("9:00"));

        conf.setDn(null);
        conf.setDx(new Time("0:40"));

        conf.setFn(new Time("9:15"));
        conf.setFx(new Time("9:20"));
        log.i(ac.toString());

        try {
            manager.calcFinalFromConfig(ac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.i("1: "+ac);


    }
}
