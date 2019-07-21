package com.flexdule;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.flexdule.android.model.sqlite.FlexduleDataBase;
import com.flexdule.android.model.sqlite.daos.ActivityDao;
import com.flexdule.android.model.sqlite.daos.CookieDao;
import com.flexdule.android.model.sqlite.daos.ScheduleDao;
import com.flexdule.android.model.sqlite.entities.ActivityBean;
import com.flexdule.android.model.sqlite.entities.CookieBean;
import com.flexdule.android.model.sqlite.entities.ScheduleBean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class DaosTest {

    public static final String tag = DaosTest.class.getSimpleName();

    private FlexduleDataBase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getContext();
        db = Room.inMemoryDatabaseBuilder(context, FlexduleDataBase.class).build();
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

        Log.i(tag, "1. Insertando cookie....");
        long insertResult = dao.save(c);
        Log.i(tag, "insertResult= " + insertResult);
        assertEquals(insertResult, 1);

        Log.i(tag, "Buscando cookie by name " + c.getName() + " ...");
        CookieBean fc = dao.findByName(c.getName());
        Log.i(tag, "foundCookie= " + fc);
        assertNotNull(fc);
        assertEquals(c, fc);

        String newName = "c1NewName";
        c.setName(newName);
        c.setValue("c1NewValue");
        c.setLabel("c1NewLabel");
        Log.i(tag, "2. Actualizando CookieBean: " + c + " ...");
        Log.i(tag, dao.save(c) + "");

        Log.i(tag, "Buscando cookie by name " + newName + " ...");
        fc = dao.findByName(newName);
        Log.i(tag, "foundCookie= " + fc);
        assertEquals(c, fc);

        Log.i(tag, "3. Borrando CookieBean: " + c + " ...");
        Log.i(tag, dao.deleteByName(newName)+"");

        Log.i(tag, "Buscando cookie by name " + newName + " ...");
        fc = dao.findByName(newName);
        Log.i(tag, "foundCookie= " + fc);
        assertNull(fc);
    }

    @Test
    public void scheduleDaoTest() {
        Log.i(tag, "BEGIN scheduleDaoTest()");
        ScheduleDao dao = db.getScheduleDao();

        ScheduleBean bean = new ScheduleBean();
        bean.setIdSchedule(1);
        bean.setColor("colorSc");
        bean.setName("nameSc");

        dao.insert(bean);
        ScheduleBean found = dao.findById(1);
        assertEquals(bean, found);
    }

    @Test
    public void activityDaoTest() {
        Log.i(tag, "BEGIN scheduleDaoTest()");
        ScheduleDao daoSch = db.getScheduleDao();
        ActivityDao dao = db.getActivityDao();

        ScheduleBean scheduleBean = new ScheduleBean();
        scheduleBean.setIdSchedule(2);
        scheduleBean.setColor("colorSc");
        scheduleBean.setName("nameSc");
        daoSch.insert(scheduleBean);

        ActivityBean bean = new ActivityBean();
        bean.setIdActivity(1);
        bean.setIdSchedule(2);
        bean.setPositionInSchedule(5);

        bean.setSn("sn");
        bean.setSx("sx");
        bean.setDn("dn");
        bean.setDx("dx");
        bean.setFn("fn");
        bean.setFx("fx");

        dao.save(bean);
        ActivityBean found = dao.findById(1);
        assertEquals(bean, found);
    }

}