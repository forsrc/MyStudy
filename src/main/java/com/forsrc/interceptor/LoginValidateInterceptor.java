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

import com.forsrc.constant.KeyConstants;
import com.forsrc.pojo.User;
import com.forsrc.utils.WebUtils;
//import com.opensymphony.xwork2.ActionInvocation;
//import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
//import org.apache.struts2.ServletActionContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;

public class LoginValidateInterceptor //extends AbstractInterceptor
        implements HandlerInterceptor {

    /**
     * @Fields serialVersionUID :
     */
    private static final long serialVersionUID = 1288902924303655764L;

    /*@Override
    public String intercept(ActionInvocation invocation) throws Exception {
        //struts2
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        WebUtils.setContentType(request, response);
        return isLogin(request, response) ? invocation.invoke() : "toLogin";

    }*/

    private boolean isLogin(HttpServletRequest request, HttpServletResponse response) {
        //HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("basePath", request.getContextPath());

        //Map<String, Object> session = ActionContext.getContext().getSession();
        HttpSession session = request.getSession();
        String time = (String) session.getAttribute(KeyConstants.LOGIN_TIME.getKey());
        User user = (User) session.getAttribute(KeyConstants.USER.getKey());

        if (null == time || null == user) {
            return false;
        }

        BigDecimal before = new BigDecimal(time);
        BigDecimal now = new BigDecimal(new Date().getTime());
        if(now.subtract(before).longValue() <= 1L * 1000 * 60 * 30){
            return true;
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //springmvc
        //springmvc
        if (isLogin(httpServletRequest, httpServletResponse)) {
            return true;
        }
        HttpSession session = httpServletRequest.getSession();
        session.removeAttribute(KeyConstants.USER.getKey());
        httpServletRequest.setAttribute("loginInfo", "relogin");
        httpServletResponse.sendRedirect("/web/login.html");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //springmvc
    }
}
