package com.flexdule;

import com.flexdule.util.U;

import org.junit.Test;

import java.time.Duration;

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
            Duration d = U.hourToDur("-1:4");
            System.out.println(d);
            System.out.println(d.isNegative());
            System.out.println(U.durToString(d));
            System.out.println(U.durToHour(d));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }
}