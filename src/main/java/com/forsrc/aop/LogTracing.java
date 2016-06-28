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

package com.forsrc.aop;

import com.forsrc.constant.KeyConstants;
import com.forsrc.utils.WebUtils;
//import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
//import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class LogTracing {

    private static final Logger LOGGER = Logger.getLogger(LogTracing.class);

    private LogTracing() {
    }

    public void doAfter(JoinPoint jp) {

        StringBuilder msg = new StringBuilder("[END]   method: ")
                .append(jp.getSignature().getName()).append("() @  ")
                .append(jp.getTarget().getClass().getName()).append(".");
        this.appendMessage(msg);
        LOGGER.info(msg);
    }

    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        long ms = System.currentTimeMillis();
        long time = System.nanoTime();
        Object retVal = pjp.proceed();
        time = System.nanoTime() - time;
        ms = System.currentTimeMillis() - ms;
        StringBuilder msg = new StringBuilder("[TIME]  method: ")
                .append(pjp.getSignature().getName()).append("() -> ")
                .append(new Double(1.0d * time / (1000000000d)).toString()).append(" s (")
                .append(ms).append(" ms) --> ")
                .append(pjp.getTarget().getClass());
        this.appendMessage(msg);
        LOGGER.info(msg);

        return retVal;
    }

    public void doBefore(JoinPoint jp) {

        StringBuilder msg = new StringBuilder("[START] method: ")
                .append(jp.getSignature().getName()).append("() @  ")
                .append(jp.getTarget().getClass().getName());
        this.appendMessage(msg);
        LOGGER.info(msg);
    }

    public void doAfterThrowing(JoinPoint jp, Throwable throwable) {

        StringBuilder msg = new StringBuilder("[THROW] method: ")
                .append(jp.getSignature().getName()).append(" : ").append(throwable.getMessage()).append("() @  ")
                .append(jp.getTarget().getClass().getName());
        this.appendMessage(msg);
        LOGGER.info(msg);
    }

    private void appendMessage(StringBuilder msg) {

        if (//ActionContext.getContext() == null &&
                RequestContextHolder.getRequestAttributes() == null) {
            return;
        }
        HttpServletRequest request =
                //ActionContext.getContext() != null
                //? (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST)
                //:
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) {
            return;
        }
        /*HttpServletResponse response = ActionContext.getContext() != null
                ? (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE)
                : ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();*/

        msg.append("; ip -> ").append(WebUtils.getIp(request));
        HttpSession session = request.getSession();
        if (session == null) {
            return;
        }
        String username = (String) session.getAttribute(KeyConstants.USERNAME.getKey());
        msg.append("; user -> ").append(username);
    }
}
