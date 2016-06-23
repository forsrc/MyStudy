package com.forsrc.utils;

import com.forsrc.constant.KeyConstants;
import com.forsrc.pojo.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;


public class SessionUtils {

    public static HttpSession getSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getSession();
    }

    public static User getUser() {
        HttpSession session = getSession();
        return getUser(session);
    }

    public static User getUser(HttpSession session) {
        Object obj = session.getAttribute(KeyConstants.USER.getKey());
        User user = obj == null ? null : (User) obj;
        return user;
    }

    public static void saveUser(HttpSession session, User user) {
        session.setAttribute(KeyConstants.USER.getKey(), user);
    }


    public static <T> T get(String key) {
        HttpSession session = getSession();
        return get(session, key);
    }

    public static <T> T get(HttpSession session, String key) {
        Object obj = session.getAttribute(key);
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    public static void saveOpToken(HttpSession session) {
        session.setAttribute(KeyConstants.OP_TOKEN.getKey(), UUID.randomUUID().toString());
    }

    public static boolean checkOpToken(HttpSession session, String token) {
        String opToken = (String) session.getAttribute(KeyConstants.OP_TOKEN.getKey());
        return token != null && token.equals(opToken);
    }
}
