package com.flexdule;

import com.flexdule.core.dtos.Time;

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


            Time t = new Time();

            t.addSeconds(11);
            System.out.println(t);
            System.out.println(t.parse(t.toString()));

            t = new Time();

            t.addMinutes(3);
            System.out.println(t);
            System.out.println(t.parse(t.toString()));
            System.out.println(t.formatHour());


            t.addHours(2);
            System.out.println(t);

            t.addSeconds(0);
            System.out.println(t);
            System.out.println(t.parse(t.toString()));

            t.addSeconds(10);
            System.out.println(t);
            System.out.println(t.formatHour());

            t.addSeconds(-10);
            System.out.println(t);
            System.out.println(t.formatHour());


            t.addMinutes(-10);
            System.out.println(t);

            t = t.parse(t.toString());
            System.out.println(t);

            t.addHours(99);
            t.addMinutes(603);
            System.out.println(t);
            System.out.println(t.parse(t.toString()));

            t.negate();
            System.out.println(t);
            System.out.println(t.parse(t.toString()));
            System.out.println(t.formatHour());


            t = t.parse("00:00:14");
            System.out.println(t);
            t.addMinutes(25);
            System.out.println(t);
            t.addHours(999);
            System.out.println(t);

            System.out.println(t.formatHour());
            System.out.println(t.parseHour(t.formatHour()));

            System.out.println(t);
            t.negate();
            t.addSeconds(-34);
            System.out.println(t.formatHour());
            System.out.println(t);




        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }
}