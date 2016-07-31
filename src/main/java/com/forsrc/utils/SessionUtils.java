package com.forsrc.utils;

import com.forsrc.constant.KeyConstants;
import com.forsrc.constant.MyToken;
import com.forsrc.pojo.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * The type Session utils.
 */
public class SessionUtils {

    /**
     * Gets session.
     *
     * @return the session
     */
    public static HttpSession getSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getSession();
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public static User getUser() {
        HttpSession session = getSession();
        return getUser(session);
    }

    /**
     * Gets user.
     *
     * @param session the session
     * @return the user
     */
    public static User getUser(HttpSession session) {
        Object obj = session.getAttribute(KeyConstants.USER.getKey());
        User user = obj == null ? null : (User) obj;
        return user;
    }

    /**
     * Save user.
     *
     * @param session the session
     * @param user    the user
     */
    public static void saveUser(HttpSession session, User user) {
        session.setAttribute(KeyConstants.USER.getKey(), user);
    }


    /**
     * Get t.
     *
     * @param <T> the type parameter
     * @param key the key
     * @return the t
     */
    public static <T> T get(String key) {
        HttpSession session = getSession();
        return get(session, key);
    }

    /**
     * Get t.
     *
     * @param <T>     the type parameter
     * @param session the session
     * @param key     the key
     * @return the t
     */
    public static <T> T get(HttpSession session, String key) {
        Object obj = session.getAttribute(key);
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    /**
     * Sets token.
     *
     * @param session the session
     */
    public static void setToken(HttpSession session) {
        MyToken token = (MyToken) session.getAttribute(KeyConstants.TOKEN.getKey());
        token.generate();
        session.setAttribute(KeyConstants.TOKEN.getKey(), token);
    }

    /**
     * Check token boolean.
     *
     * @param session the session
     * @param myToken the my token
     * @return the boolean
     */
    public static boolean checkToken(HttpSession session, MyToken myToken) {
        MyToken token = (MyToken) session.getAttribute(KeyConstants.TOKEN.getKey());
        return token.getLoginToken().equals(myToken.getLoginToken());
    }

}
