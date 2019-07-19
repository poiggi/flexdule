package com.flexdule.android.manager;

import android.content.Context;
import android.util.Log;

import com.flexdule.core.manager.ActivityAccessManager;
import com.flexdule.core.dtos.Activity;
import com.flexdule.core.dtos.ActivityVars;
import com.flexdule.android.model.sqlite.daos.ActivityDao;
import com.flexdule.android.model.sqlite.entities.ActivityBean;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AndroidActivityAccessManager implements ActivityAccessManager {

    private static final String tag = AndroidActivityAccessManager.class.getSimpleName();

    public AndroidActivityAccessManager(Context context) throws Exception {
        try {
            AccessContext.createDataBase(context);
        } catch (Exception e) {
            Log.e(tag, "Error in AndroidActivityAccessManager: " + e);
            throw e;
        }
    }

    @Override
    public List<Activity> findBySchedule(Integer idSchedule) throws Exception {
        List<Activity> activities = null;

        try {
            List<ActivityBean> activityBeans = getDao().findBySchedule(idSchedule);
            activities = activityBeansToActivities(activityBeans);
        } catch (Exception e) {
            Log.e(tag, "Error en findBySchedule()= " + e);
            throw e;
        }
        Log.i(tag, "END findBySchedule(). activities=" + activities);
        return activities;
    }

    @Override
    public List<Activity> findAll() throws Exception {
        List<Activity> activities = null;

        try {
            List<ActivityBean> activityBeans = getDao().findAll();
            activities = activityBeansToActivities(activityBeans);
        } catch (Exception e) {
            Log.e(tag, "Error en findAll()= " + e);
            throw e;
        }
        Log.i(tag, "END findAll(). foundSize=" + activities.size());
        return activities;
    }

    @Override
    public long save(Activity activity) throws Exception {
        long result;
        try {
            ActivityBean bean = activityToActivityBean(activity);
            result = getDao().save(bean);
        } catch (Exception e) {
            Log.e(tag, "Error en save()= " + e);
            throw e;
        }
        Log.i(tag, "END save(). result=" + result);
        return result;
    }

    @Override
    public List<Long> save(List<Activity> activities) throws Exception {
        List<Long> result;
        try {
            List<ActivityBean> beans = activitiesToActivityBeans(activities);
            result = getDao().save(beans);
        } catch (Exception e) {
            Log.e(tag, "Error en save()= " + e);
            throw e;
        }
        Log.i(tag, "END save(). result=" + result);
        return result;
    }

    @Override
    public int deleteById(Integer idActivity) throws Exception {
        Integer result = null;
        try{
            result = getDao().deleteById(idActivity);
        }catch(Exception e){
            Log.e(tag, "Error en deleteById()= " + e);
            throw e;
        }
        Log.i(tag, "END deleteById(). result=" + result);
        return result;
    }


    public static List<Activity> activityBeansToActivities(List<ActivityBean> activityBeans) {
        List<Activity> acs = new ArrayList<>();

        if(activityBeans!=null) {
            for (ActivityBean bean : activityBeans) {
                Activity ac = activityBeanToActivity(bean);
                acs.add(ac);
            }
        }
        return acs;
    }


    public static List<ActivityBean> activitiesToActivityBeans(List<Activity> activities) {
        List<ActivityBean> beans = new ArrayList<>();

        if(activities!=null) {
            for (Activity ac: activities) {
                ActivityBean bean = activityToActivityBean(ac);
                beans.add(bean);
            }
        }
        return beans;
    }

    public static Activity activityBeanToActivity(ActivityBean activityBean){
        Activity ac = null;

        if(activityBean != null){
            ac = new Activity();

            ac.setIdActivity(activityBean.getIdActivity());
            ac.setIdSchedule(activityBean.getIdSchedule());
            ac.setName(activityBean.getName());
            ac.setColor(activityBean.getColor());
            ac.setPositionInSchedule(activityBean.getPositionInSchedule());

            ActivityVars durs = new ActivityVars();
            if (activityBean.getSn() != null) durs.setSn(Duration.parse(activityBean.getSn()));
            if (activityBean.getSx() != null) durs.setSx(Duration.parse(activityBean.getSx()));
            if (activityBean.getDn() != null) durs.setDn(Duration.parse(activityBean.getDn()));
            if (activityBean.getDx() != null) durs.setDx(Duration.parse(activityBean.getDx()));
            if (activityBean.getFn() != null) durs.setFn(Duration.parse(activityBean.getFn()));
            if (activityBean.getFx() != null) durs.setFx(Duration.parse(activityBean.getFx()));
            ac.setConfigVars(durs);

            ActivityVars dursF = new ActivityVars();
            if (activityBean.getSn() != null) dursF.setSn(Duration.parse(activityBean.getSn()));
            if (activityBean.getSx() != null) dursF.setSx(Duration.parse(activityBean.getSx()));
            if (activityBean.getDn() != null) dursF.setDn(Duration.parse(activityBean.getDn()));
            if (activityBean.getDx() != null) dursF.setDx(Duration.parse(activityBean.getDx()));
            if (activityBean.getFn() != null) dursF.setFn(Duration.parse(activityBean.getFn()));
            if (activityBean.getFx() != null) dursF.setFx(Duration.parse(activityBean.getFx()));
            ac.setFinalVars(dursF);
        }
        return ac;
    }


    public static ActivityBean activityToActivityBean(Activity activity){
        ActivityBean bean = null;

        if(activity!=null){
            bean = new ActivityBean();

            bean.setIdActivity(activity.getIdActivity());
            bean.setIdSchedule(activity.getIdSchedule());
            bean.setName(activity.getName());
            bean.setColor(activity.getColor());
            bean.setPositionInSchedule(activity.getPositionInSchedule());

            if (activity.getConfigVars().getSn() != null)
                bean.setSn(activity.getConfigVars().getSn().toString());
            if (activity.getConfigVars().getSx() != null)
                bean.setSx(activity.getConfigVars().getSx().toString());
            if (activity.getConfigVars().getDn() != null)
                bean.setDn(activity.getConfigVars().getDn().toString());
            if (activity.getConfigVars().getDx() != null)
                bean.setDx(activity.getConfigVars().getDx().toString());
            if (activity.getConfigVars().getFn() != null)
                bean.setFn(activity.getConfigVars().getFn().toString());
            if (activity.getConfigVars().getFx() != null)
                bean.setFx(activity.getConfigVars().getFx().toString());
        }
        return bean;
    }

    private ActivityDao getDao() throws Exception {
        ActivityDao dao=null;
        try {
            dao = AccessContext.getDataBase().getActivityDao();
        }catch (Exception e){
            Log.e(tag, "Error en getDao()= " + e);
            throw e;
        }
        return dao;
    }

}
