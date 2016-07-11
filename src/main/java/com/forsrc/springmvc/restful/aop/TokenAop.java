package com.forsrc.springmvc.restful.aop;


import com.forsrc.aop.LogTracing;
import com.forsrc.constant.KeyConstants;
import com.forsrc.constant.MyToken;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class TokenAop {

    private static final Logger LOGGER = Logger.getLogger(LogTracing.class);

    public void doAfter(JoinPoint jp) {
    }

    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        if (RequestContextHolder.getRequestAttributes() == null) {
            return proceedingJoinPoint.proceed();
        }
        HttpServletRequest request = null;
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object object : args) {
            if (object instanceof HttpServletRequest) {
                request = (HttpServletRequest) object;
            }
        }
        //request =  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) {
            return proceedingJoinPoint.proceed();
        }
        HttpSession session = request.getSession();
        if (session == null) {
            return proceedingJoinPoint.proceed();
        }
        ModelAndView modelAndView = new ModelAndView();
        Map<String, String> message = new HashMap<String, String>();
        modelAndView.addObject("return", message);
        modelAndView.addObject("status", 401);
        String token = request.getParameter("token");

        if(StringUtils.isBlank(token)){
            message.put("message", "Missing token");
            return modelAndView;
        }
        MyToken myToken = (MyToken) session.getAttribute(KeyConstants.TOKEN.getKey());
        if (token.equals(myToken.getToken())) {
            message.put("message", "Invalid token");
            return modelAndView;
        }

        return proceedingJoinPoint.proceed();
    }

    public void doBefore(JoinPoint jp) {
    }

    public void doAfterThrowing(JoinPoint jp, Throwable throwable) {

    }

}
