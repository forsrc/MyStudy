package com.forsrc.utils;


import org.springframework.context.MessageSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class MessageUtils {
    public static void setMessage(HttpServletRequest request, String key, String msg) {
        request.setAttribute(key, msg);
    }

    public static String getText(MessageSource messageSource, String key, Object[] params) {
        Locale locale = getLocale();
        return messageSource.getMessage(key, params, locale);
    }

    public static String getText(MessageSource messageSource, String key) {

        return messageSource.getMessage(key, null, getLocale());
    }

    public static Locale getLocale() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(httpServletRequest);

        if (localeResolver != null) {
            return localeResolver.resolveLocale(httpServletRequest);
        }

        RequestContext requestContext = new RequestContext(httpServletRequest);
        return requestContext.getLocale();
    }
}
