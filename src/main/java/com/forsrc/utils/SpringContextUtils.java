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

public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext paramApplicationContext) {
        if (applicationContext == null) {
            applicationContext = paramApplicationContext;
        }
    }

    public static ApplicationContext getApplicationContext(String xml) {
        String xmlFile = xml == null ? "applicationContext.xml" : xml;
        return new FileSystemXmlApplicationContext(xmlFile);
    }

    public static ApplicationContext getWebApplicationContext() {

        return ContextLoader.getCurrentWebApplicationContext();
    }

    public static ApplicationContext getApplicationContext(HttpServletRequest request) {
        ServletContext servletContext = request.getSession().getServletContext();
        return getApplicationContext(servletContext);
    }

    public static ApplicationContext getApplicationContext(ServletContext sc) {

        return WebApplicationContextUtils.getWebApplicationContext(sc);
    }

    public static Object getBean(String id) {
        return applicationContext.getBean(id);

    }

    public static boolean containsBean(String beanid) {
        return applicationContext.containsBean(beanid);
    }

    public static UserDetails getUserPrincipal() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();

        return user;
    }

    public static List<Object> getAllPrincipals() {
        SessionRegistry sessionRegistry = (SessionRegistry) SpringContextUtils.getBean("sessionRegistry");
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            sessionRegistry.getAllSessions(principal, true).get(0).getLastRequest();
        }
        return principals;
    }

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