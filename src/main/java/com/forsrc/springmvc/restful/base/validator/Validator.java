/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forsrc.springmvc.restful.base.validator;


import com.forsrc.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class Validator {
    protected HttpServletRequest request;

    protected ModelAndView modelAndView;

    protected Map<String, String> map;

    @Autowired
    @Resource(name = "messageSource")
    protected MessageSource messageSource;

    public Validator(HttpServletRequest request, ModelAndView modelAndView) {
        this.request = request;
        this.modelAndView = modelAndView;
        this.map = new HashMap<String, String>();
        this.modelAndView.addObject(this.map);
    }

    protected void setMessage(HttpServletRequest request, String key, String msg) {
        request.setAttribute(key, msg);
    }

    protected String getText(String key, Object[] params) {
        Locale locale = getLocale();
        return messageSource.getMessage(key, params, locale);
    }

    protected String getText(String key) {

        return messageSource.getMessage(key, null, getLocale());
    }

    private Locale getLocale() {
        return MessageUtils.getLocale();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ModelAndView getModelAndView() {
        return modelAndView;
    }

    public void setModelAndView(ModelAndView modelAndView) {
        this.modelAndView = modelAndView;
    }
}
