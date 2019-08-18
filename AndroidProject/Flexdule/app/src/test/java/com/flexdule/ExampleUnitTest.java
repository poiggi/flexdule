package com.flexdule;

import com.flexdule.core.dtos.Activity;
import com.flexdule.core.dtos.ActivityVars;
import com.flexdule.core.util.Time;

import org.junit.Test;

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

            boolean b = false || true && true;
            System.out.println(b);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }

}