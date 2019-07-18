package com.flexdule;

import com.flexdule.core.utils.CU;
import com.flexdule.core.dtos.Activity;

import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() {

        try {

            Duration d = Duration.parse("PT9H");
            System.out.println(CU.durToHour(d));
            ArrayList<Activity> acs = new ArrayList<>();

            Activity ac1 = new Activity();
            ac1.getConfigVars().setDn(CU.hourToDur("10:20"));
            System.out.println(ac1);
//            acs.sort(Comparator.comparing(Activity::getPositionInSchedule));


            System.out.println(String.valueOf(null));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }
}