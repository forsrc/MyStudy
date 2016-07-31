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
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The type Validator.
 */
@Component
public abstract class Validator {
    /**
     * The Request.
     */
    protected HttpServletRequest request;

    /**
     * The Error message.
     */
    protected Map<String, Object> errorMessage;

    /**
     * The Message.
     */
    protected Map<String, Object> message;

    /**
     * The Message source.
     */
    protected MessageSource messageSource;

    /**
     * Instantiates a new Validator.
     *
     * @param request       the request
     * @param errorMessage  the error message
     * @param messageSource the message source
     */
    public Validator(HttpServletRequest request, Map<String, Object> errorMessage, MessageSource messageSource) {
        this.request = request;
        this.message = new HashMap<String, Object>();
        this.errorMessage.put("return", this.message);
        this.messageSource = messageSource;
    }

    /**
     * Instantiates a new Validator.
     *
     * @param request       the request
     * @param messageSource the message source
     */
    public Validator(HttpServletRequest request, MessageSource messageSource) {
        this.request = request;
        this.errorMessage = new HashMap<String, Object>();
        this.message = new HashMap<String, Object>();
        this.errorMessage.put("return", this.message);
        this.messageSource = messageSource;
    }

    /**
     * Instantiates a new Validator.
     *
     * @param request the request
     */
    public Validator(HttpServletRequest request) {
        this.request = request;
        this.errorMessage = new HashMap<String, Object>();
        this.message = new HashMap<String, Object>();
        this.errorMessage.put("return", this.message);
    }

    /**
     * Sets message.
     *
     * @param request the request
     * @param key     the key
     * @param msg     the msg
     */
    protected void setMessage(HttpServletRequest request, String key, String msg) {
        request.setAttribute(key, msg);
    }

    /**
     * Gets text.
     *
     * @param key    the key
     * @param params the params
     * @return the text
     */
    protected String getText(String key, Object[] params) {
        Locale locale = getLocale();
        return messageSource.getMessage(key, params, locale);
    }

    /**
     * Gets text.
     *
     * @param key the key
     * @return the text
     */
    protected String getText(String key) {

        return messageSource.getMessage(key, null, getLocale());
    }

    private Locale getLocale() {
        return MessageUtils.getLocale();
    }

    /**
     * Gets request.
     *
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Sets request.
     *
     * @param request the request
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Gets message source.
     *
     * @return the message source
     */
    public MessageSource getMessageSource() {
        return messageSource;
    }

    /**
     * Sets message source.
     *
     * @param messageSource the message source
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Gets error message.
     *
     * @return the error message
     */
    public Map<String, Object> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets error message.
     *
     * @param errorMessage the error message
     */
    public void setErrorMessage(Map<String, Object> errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public Map<String, Object> getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(Map<String, Object> message) {
        this.message = message;
    }
}
