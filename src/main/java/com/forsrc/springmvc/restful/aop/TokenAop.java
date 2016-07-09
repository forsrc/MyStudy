package com.forsrc.springmvc.restful.aop;


import com.forsrc.aop.LogTracing;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class TokenAop {

    private static final Logger LOGGER = Logger.getLogger(LogTracing.class);

    public void doAfter(JoinPoint jp) {
    }

    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        if (RequestContextHolder.getRequestAttributes() == null) {
            return pjp.proceed();
        }
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) {
            return pjp.proceed();
        }
        HttpSession session = request.getSession();
        if (session == null) {
            return pjp.proceed();
        }
        String token = request.getParameter("token");
        if(StringUtils.isBlank(token)){
            return null;
        }
        return pjp.proceed();
    }

    public void doBefore(JoinPoint jp) {
    }

    public void doAfterThrowing(JoinPoint jp, Throwable throwable) {

    }

}
