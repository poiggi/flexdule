package flexdule.core.model.managers;

import java.util.List;

import flexdule.core.dtos.Cookie;

public interface CookieAccesManager {


    List<Cookie> findAllCookies() throws Exception;

    Cookie findCookieByName(String name) throws Exception;

    long saveCookie(Cookie cookie) throws Exception;

    int deleteCookieByName(String name) throws Exception;
}
