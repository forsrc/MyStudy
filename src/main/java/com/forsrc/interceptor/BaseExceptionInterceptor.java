/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.forsrc.interceptor;

import com.forsrc.exception.ActionException;
import com.forsrc.exception.RollbackException;
import com.forsrc.exception.ServiceException;
//import com.opensymphony.xwork2.ActionContext;
//import com.opensymphony.xwork2.ActionInvocation;
//import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.log4j.Logger;
//import org.apache.struts2.ServletActionContext;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class BaseExceptionInterceptor
        //extends AbstractInterceptor
        implements HandlerExceptionResolver {

    private static final Logger LOGGER = Logger
            .getLogger(BaseExceptionInterceptor.class);

    /**
     *
     */
    private static final long serialVersionUID = -896488463833135298L;

    /*@Override
    public String intercept(ActionInvocation invocation) throws Exception {
        //struts2
        HttpServletRequest request = ServletActionContext.getRequest();
        try {
            return invocation.invoke();
        } catch (RollbackException e) {
            e.printStackTrace();
            String message = MessageFormat.format("Rollback: {0}.{1}.{2}", this
                    .getClass().getName(), invocation.getProxy()
                    .getActionName(), invocation.getProxy().getMethod());
            LOGGER.info(message);
            ActionContext actionContext = invocation.getInvocationContext();
            actionContext.put("rollbackMessage", message);

            request.setAttribute("message", e.getMessage());
            request.setAttribute("error", e);
            return "rollbackException";
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            request.setAttribute("message", e.getMessage());
            request.setAttribute("error", e);
            return "exception";
        }

    }*/

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        //springmvc
        request.setAttribute("message", e.getMessage());
        request.setAttribute("status", 400);
        request.setAttribute("exception", e);
        request.setAttribute("exceptionClass", e.getClass());
        request.setAttribute("at", e.getStackTrace()[0].toString());
        if (e.getMessage() == null || "".equals(e.getMessage())) {
            LOGGER.error(e.getMessage(), e);
        } else {
            LOGGER.error(MessageFormat.format("{0} --> at {1}", e.getMessage(), e.getStackTrace()[0].toString()));
        }
        e.printStackTrace();
        request.getSession().setAttribute("hasError", true);
        request.getSession().setAttribute("status", 400);
        request.getSession().setAttribute("message", e.getMessage());
        request.getSession().setAttribute("error", e);
        if (e instanceof RollbackException) {
//            String message = String.format("Rollback: %s.%s.%s", this
//                    .getClass().getName(), invocation.getProxy()
//                    .getActionName(), invocation.getProxy().getMethod());
//            LOGGER.info(message);
//
//            request.setAttribute("rollbackMessage", message);

            //return null;

        }
        if (e instanceof ServiceException) {
//            String message = String.format("Rollback: %s.%s.%s", this
//                    .getClass().getName(), invocation.getProxy()
//                    .getActionName(), invocation.getProxy().getMethod());
//            LOGGER.info(message);
//
//            request.setAttribute("rollbackMessage", message);
//
            //return null;
        }

        if (e instanceof ActionException) {

//            String message = String.format("Rollback: %s.%s.%s", this
//                    .getClass().getName(), invocation.getProxy()
//                    .getActionName(), invocation.getProxy().getMethod());
//            LOGGER.info(message);
//
//            request.setAttribute("rollbackMessage", message);
            //String url = request.getRequestURI();
            //return null;
        }
        ModelAndView modelAndView = new ModelAndView("/error.html");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", e.getMessage());
        map.put("exceptionClass", e.getClass());
        map.put("hasException", true);
        map.put("status", 400);
        map.put("at", e.getStackTrace()[0].toString());
        //map.put("error", e);
        if (e instanceof NullPointerException) {
            map.put("message", "NullPointerException: " + e.getMessage());
        }
        modelAndView.addObject("error", map);
        //modelAndView.getModelMap().put("error", map);
        return modelAndView;
    }
}
