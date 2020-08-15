package me.liiot.snsserver.controller.util;

import me.liiot.snsserver.model.User;

import javax.servlet.http.HttpSession;

public class SessionUtil {

    public static final String USER = "user";

    public static void setUser(HttpSession httpSession, Object object) {
        httpSession.setAttribute(USER, object);
    }

    public static User getUser(HttpSession httpSession) {
        User user = (User)httpSession.getAttribute(USER);
        return user;
    }

    public static void deleteAllAttribute(HttpSession httpSession) {
        httpSession.invalidate();
    }
}
