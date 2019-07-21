package com.flexdule.core.manager;

import com.flexdule.core.dtos.Cookie;

import java.util.List;

public interface CookieAccesManager {


    List<Cookie> findAllCookies() throws Exception;

    Cookie findCookieByName(String name) throws Exception;

    long saveCookie(Cookie cookie) throws Exception;

    int deleteCookieByName(String name) throws Exception;
}
