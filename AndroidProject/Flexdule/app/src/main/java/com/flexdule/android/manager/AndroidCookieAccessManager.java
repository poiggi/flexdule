package com.flexdule.android.manager;

import android.content.Context;
import android.util.Log;

import com.flexdule.core.dtos.Cookie;
import com.flexdule.core.manager.CookieAccesManager;
import com.flexdule.android.model.sqlite.daos.CookieDao;
import com.flexdule.android.model.sqlite.entities.CookieBean;

import java.util.ArrayList;
import java.util.List;

public class AndroidCookieAccessManager implements CookieAccesManager {
    private static final String tag = AndroidCookieAccessManager.class.getSimpleName();


    public AndroidCookieAccessManager(Context context) throws Exception {
        try {
            AccessContext.createDataBase(context);
        } catch (Exception e) {
            Log.e(tag, "Error in AndroidCookieAccessManager: " + e);
            throw e;
        }
    }

    @Override
    public List<Cookie> findAll() throws Exception {
        List<Cookie> schedules = null;

        try {
            List<CookieBean> cookieBeans = getDao().findAll();
            schedules = cookieBeansToCookies(cookieBeans);
        } catch (Exception e) {
            Log.e(tag, "Error en findAll()= " + e);
            throw e;
        }
        Log.i(tag, "END findAll(). foundSize=" + schedules.size());
        return schedules;
    }

    @Override
    public Cookie findById(Integer idSchedule) throws Exception {
        Cookie cookie = null;

        try {
            CookieBean scheduleBean = getDao().findById(idSchedule);
            cookie = cookieBeanToCookie(scheduleBean);
        } catch (Exception e) {
            Log.e(tag, "Error en findById()= " + e);
            throw e;
        }
        Log.i(tag, "END findById(). cookie=" + cookie);
        return cookie;
    }

    @Override
    public long save(Cookie cookie) throws Exception{
        long result = 0;
        try{
            CookieBean bean = cookieToCookieBean(cookie);
            result= getDao().save(bean);
        }catch (Exception e){
            Log.e(tag, "Error in save()= " + e);
            throw e;
        }
        Log.i(tag, "END save(). result=" + result);
        return result;
    }

    @Override
    public int deleteById(Integer idCookie) throws Exception {
        Integer result = null;
        try {
            result = getDao().deleteById(idCookie);
        } catch (Exception e) {
            Log.e(tag, "Error in deleteById()= " + e);
            throw e;
        }
        Log.i(tag, "END deleteById(). result=" + result);
        return result;
    }

    public static List<Cookie> cookieBeansToCookies(List<CookieBean> cookieBeans) {
        List<Cookie> cos = new ArrayList<>();

        if(cookieBeans!=null) {
            for (CookieBean bean : cookieBeans) {
                Cookie sc = cookieBeanToCookie(bean);
                cos.add(sc);
            }
        }
        return cos;
    }

    public static List<CookieBean> cookiesToCookieBeans(List<Cookie> cookies) {
        List<CookieBean> beans = new ArrayList<>();

        if(cookies!=null) {
            for (Cookie item: cookies) {
                CookieBean bean = cookieToCookieBean(item);
                beans.add(bean);
            }
        }
        return beans;
    }

    public static Cookie cookieBeanToCookie(CookieBean cookieBean){
        Cookie co = null;

        if(cookieBean != null){
            co = new Cookie();

            co.setIdCookie(cookieBean.getIdCookie());
            co.setLabel(cookieBean.getLabel());
            co.setName(cookieBean.getName());
            co.setValue(cookieBean.getValue());
        }
        return co;
    }


    public static CookieBean cookieToCookieBean(Cookie cookie){
        CookieBean bean = null;

        if(cookie!=null){
            bean = new CookieBean();

            bean.setIdCookie(cookie.getIdCookie());
            bean.setLabel(cookie.getLabel());
            bean.setName(cookie.getName());
            bean.setValue(cookie.getValue());
        }
        return bean;
    }

    private CookieDao getDao() throws Exception {
        CookieDao dao=null;
        try {
            dao = AccessContext.getDataBase().getCookieDao();
        }catch (Exception e){
            Log.e(tag, "Error in getDao()= " + e);
            throw e;
        }
        return dao;
    }
}
