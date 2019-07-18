package com.flexdule.android.manager;

import android.content.Context;
import android.util.Log;

import com.flexdule.core.dtos.Schedule;
import com.flexdule.core.manager.ScheduleAccessManager;
import com.flexdule.android.model.sqlite.FlexduleDataBase;
import com.flexdule.android.model.sqlite.daos.ScheduleDao;
import com.flexdule.android.model.sqlite.entities.ScheduleBean;

import java.util.ArrayList;
import java.util.List;

public class AndroidScheduleAccessManager implements ScheduleAccessManager {
    private static final String tag = AndroidScheduleAccessManager.class.getSimpleName();

    private FlexduleDataBase db;

    public AndroidScheduleAccessManager(Context context) throws Exception{
        try {
            AccessContext.createDataBase(context);
        } catch (Exception e) {
            Log.e(tag,"Error in AndroidScheduleAccessManager: "+e);
            throw e;
        }
    }

    @Override
    public Schedule findFirstOne() throws Exception {
        Schedule schedule = null;

        try {
            ScheduleBean scheduleBean = getDao().findFirstOne();
            schedule = scheduleBeanToSchedule(scheduleBean);
        } catch (Exception e) {
            Log.e(tag, "Error in findFirstOne()= " + e);
            throw e;
        }
        Log.i(tag, "END findById(). schedule=" + schedule);
        return schedule;
    }

    @Override
    public Schedule findById(Integer idSchedule) throws Exception {
        Schedule schedule = null;

        try {
            ScheduleBean scheduleBean = getDao().findById(idSchedule);
            schedule = scheduleBeanToSchedule(scheduleBean);
        } catch (Exception e) {
            Log.e(tag, "Error in findById()= " + e);
            throw e;
        }
        Log.i(tag, "END findById(). schedule=" + schedule);
        return schedule;
    }

    @Override
    public List<Schedule> findAll() throws Exception {
        List<Schedule> schedules = null;

        try {
            List<ScheduleBean> activityBeans = getDao().findAll();
            schedules = scheduleBeansToSchedules(activityBeans);
        } catch (Exception e) {
            Log.e(tag, "Error in findAll()= " + e);
            throw e;
        }
        Log.i(tag, "END findAll(). foundSize=" + schedules.size());
        return schedules;
    }

    @Override
    public long save(Schedule schedule) throws Exception{
        long result = 0;
        try{
            ScheduleBean bean = scheduleToScheduleBean(schedule);
            result= getDao().save(bean);
        }catch (Exception e){
            Log.e(tag, "Error in save()= " + e);
            throw e;
        }
        Log.i(tag, "END save(). result=" + result);
        return result;

    }

    @Override
    public int deleteById(Integer idSchedule) throws Exception {
        Integer result = null;
        try{
            result = getDao().deleteById(idSchedule);
        }catch(Exception e){
            Log.e(tag, "Error in deleteById()= " + e);
            throw e;
        }
        Log.i(tag, "END deleteById(). result=" + result);
        return result;
    }


    public static List<Schedule> scheduleBeansToSchedules(List<ScheduleBean> scheduleBeans) {
        List<Schedule> scs = new ArrayList<>();

        if(scheduleBeans!=null) {
            for (ScheduleBean bean : scheduleBeans) {
                Schedule sc = scheduleBeanToSchedule(bean);
                scs.add(sc);
            }
        }
        return scs;
    }

    public static List<ScheduleBean> schedulesToScheduleBeans(List<Schedule> schedules) {
        List<ScheduleBean> beans = new ArrayList<>();

        if(schedules!=null) {
            for (Schedule item: schedules) {
                ScheduleBean bean = scheduleToScheduleBean(item);
                beans.add(bean);
            }
        }
        return beans;
    }

    public static Schedule scheduleBeanToSchedule(ScheduleBean scheduleBean){
        Schedule sc = null;

        if(scheduleBean != null){
            sc = new Schedule();

            sc.setIdSchedule(scheduleBean.getIdSchedule());
            sc.setName(scheduleBean.getName());
            sc.setColor(scheduleBean.getColor());
        }
        return sc;
    }


    public static ScheduleBean scheduleToScheduleBean(Schedule schedule){
        ScheduleBean bean = null;

        if(schedule!=null){
            bean = new ScheduleBean();

            bean.setIdSchedule(schedule.getIdSchedule());
            bean.setName(schedule.getName());
            bean.setColor(schedule.getColor());
        }
        return bean;
    }

    private ScheduleDao getDao() throws Exception {
        ScheduleDao dao=null;
        try {
            dao = AccessContext.getDataBase().getScheduleDao();
        }catch (Exception e){
            Log.e(tag, "Error in getDao()= " + e);
            throw e;
        }
        return dao;
    }
}
