package com.flexdule;

import com.flexdule.core.dtos.ActivityVars;
import com.flexdule.core.dtos.NX;
import com.flexdule.core.util.AppColors;
import com.flexdule.core.util.CU;
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

            ActivityVars var1 = new ActivityVars();
            var1.setFn(Duration.ofHours(1));
            Activity ac = new Activity();
            ac.setFinalVars(var1);
            ac.setConfigVars(var1);
            System.out.println(ac);

            var1.setFn(Duration.ofHours(2));
            System.out.println(ac);

            ac.getFinalVars().setFn(Duration.ofHours(3));
            System.out.println(ac);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }
}