package com.flexdule;

import com.flexdule.core.dtos.ActivityVars;
import com.flexdule.core.dtos.NX;
import com.flexdule.core.util.AppColors;
import com.flexdule.core.util.CU;
import com.flexdule.core.dtos.Activity;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
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

            Duration d = CU.hourToDur("12:6");
            d = d.minus(d);
            d.plusHours(1);
            System.out.println(d);


        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }
}