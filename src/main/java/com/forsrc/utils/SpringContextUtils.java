package com.forsrc.utils;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * The type Spring context utils.
 */
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * Gets application context.
     *
     * @return the application context
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext paramApplicationContext) {
        if (applicationContext == null) {
            applicationContext = paramApplicationContext;
        }
    }

    /**
     * Gets application context.
     *
     * @param xml the xml
     * @return the application context
     */
    public static ApplicationContext getApplicationContext(String xml) {
        String xmlFile = xml == null ? "applicationContext.xml" : xml;
        return new FileSystemXmlApplicationContext(xmlFile);
    }

    /**
     * Gets web application context.
     *
     * @return the web application context
     */
    public static ApplicationContext getWebApplicationContext() {

        return ContextLoader.getCurrentWebApplicationContext();
    }

    /**
     * Gets application context.
     *
     * @param request the request
     * @return the application context
     */
    public static ApplicationContext getApplicationContext(HttpServletRequest request) {
        ServletContext servletContext = request.getSession().getServletContext();
        return getApplicationContext(servletContext);
    }

    /**
     * Gets application context.
     *
     * @param sc the sc
     * @return the application context
     */
    public static ApplicationContext getApplicationContext(ServletContext sc) {

        return WebApplicationContextUtils.getWebApplicationContext(sc);
    }

    /**
     * Gets bean.
     *
     * @param id the id
     * @return the bean
     */
    public static Object getBean(String id) {
        return applicationContext.getBean(id);

    }

    /**
     * Contains bean boolean.
     *
     * @param beanid the beanid
     * @return the boolean
     */
    public static boolean containsBean(String beanid) {
        return applicationContext.containsBean(beanid);
    }

    /**
     * Gets user principal.
     *
     * @return the user principal
     */
    public static UserDetails getUserPrincipal() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();

        return user;
    }

    /**
     * Gets all principals.
     *
     * @return the all principals
     */
    public static List<Object> getAllPrincipals() {
        SessionRegistry sessionRegistry = (SessionRegistry) SpringContextUtils.getBean("sessionRegistry");
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            sessionRegistry.getAllSessions(principal, true).get(0).getLastRequest();
        }
        return principals;
    }

    /**
     * Expire boolean.
     *
     * @param username the username
     * @return the boolean
     */
    public static boolean expire(String username) {

        SessionRegistry sessionRegistry = (SessionRegistry) SpringContextUtils.getBean("sessionRegistry");
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            UserDetails user = (UserDetails) principal;
            if (user.getUsername().equals(username)) {
                List<SessionInformation> sessionInfos = sessionRegistry.getAllSessions(principal, true);
                for (SessionInformation sessionInfo : sessionInfos) {
                    sessionInfo.expireNow();
                }
                return true;
            }
        }
        return false;
    }
}