package com.flexdule.core.manager;

import com.flexdule.core.dtos.Cookie;

import java.util.List;

public interface CookieAccesManager {

    long save(Cookie cookie) throws Exception;

    List<Cookie> findAll() throws Exception;

    Cookie findById(Integer idSchedule) throws Exception;

    int deleteById(Integer idCookie) throws Exception;
}
