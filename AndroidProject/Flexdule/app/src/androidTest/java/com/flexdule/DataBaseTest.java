package com.flexdule;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.flexdule.model.sqlite.AppDataBase;
import com.flexdule.model.sqlite.daos.ActivityDao;
import com.flexdule.model.sqlite.daos.CookieDao;
import com.flexdule.model.sqlite.daos.ScheduleDao;
import com.flexdule.model.sqlite.entities.ActivityBean;
import com.flexdule.model.sqlite.entities.CookieBean;
import com.flexdule.model.sqlite.entities.ScheduleBean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class DataBaseTest {

    public static final String tag = DataBaseTest.class.getSimpleName();

    private AppDataBase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDataBase.class).build();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void cookieDaoTest() {
        Log.i(tag, "BEGIN cookieDaoTest()");
        CookieDao dao = db.getCookieDao();

        CookieBean c = new CookieBean();
        c.setName("ctest");
        c.setValue("cValue");
        c.setLabel("cLabel");
        Log.i(tag, "CookieBean creada: " + c);

        Log.i(tag, "Insertando cookie....");
        long insertResult = dao.insert(c);
        Log.i(tag, "insertResult= " + insertResult);
        assertEquals(insertResult, 1);

        Log.i(tag, "Buscando cookie by name " + c.getName() + " ...");
        CookieBean fc = dao.findByName(c.getName());
        Log.i(tag, "foundCookie= " + fc);
        assertNotNull(fc);
        c.setIdCookie(fc.getIdCookie());
        assertEquals(c, fc);

        c.setValue("c1NewValue");
        c.setLabel("c1NewLabel");
        Log.i(tag, "Actualizando CookieBean: " + c + " ...");
        Log.i(tag, dao.update(c) + "");

        Log.i(tag, "Buscando cookie by name " + c.getName() + " ...");
        fc = dao.findByName(c.getName());
        Log.i(tag, "foundCookie= " + fc);
        assertEquals(fc, c);

        CookieBean cd = new CookieBean();
        cd.setIdCookie(fc.getIdCookie());
        Log.i(tag, "Borrando CookieBean: " + c + " ...");
        Log.i(tag, dao.delete(cd) + "");

        Log.i(tag, "Buscando cookie by name " + c.getName() + " ...");
        fc = dao.findByName(c.getName());
        Log.i(tag, "foundCookie= " + fc);
        assertNull(fc);
    }

    @Test
    public void scheduleDaoTest() {
        Log.i(tag, "BEGIN scheduleDaoTest()");
        ScheduleDao dao = db.getScheduleDao();

        ScheduleBean bean = new ScheduleBean();
        bean.setIdSchedule(999999);
        bean.setColor("colorSc");
        bean.setName("nameSc");

        dao.insert(bean);
        ScheduleBean found = dao.findById(999999);
        assertEquals(bean, found);
    }

    @Test
    public void activityDaoTest() {
        Log.i(tag, "BEGIN scheduleDaoTest()");
        ActivityDao dao = db.getActivityDao();

        ActivityBean bean = new ActivityBean();
        bean.setIdActivity(999998);
        bean.setIdSchedule(999997);
        bean.setPositionInSchedule(5);

        bean.setSn("sn");
        bean.setSx("sx");
        bean.setDn("dn");
        bean.setDx("dx");
        bean.setFn("fn");
        bean.setFx("fx");

        dao.insert(bean);
        ActivityBean found = dao.findById(999998);
        assertEquals(bean, found);
    }
}