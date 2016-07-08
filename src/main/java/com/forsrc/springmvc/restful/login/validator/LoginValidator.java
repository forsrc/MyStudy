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
package com.forsrc.springmvc.restful.login.validator;


import com.forsrc.constant.KeyConstants;
import com.forsrc.constant.MyToken;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.base.validator.Validator;
import com.forsrc.utils.MyStringUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginValidator extends Validator {

    private User user;
    private String loginToken;

    public LoginValidator(HttpServletRequest request, ModelAndView modelAndView, MessageSource messageSource) {
        super(request, modelAndView, messageSource);
    }

    public LoginValidator(User user, String loginToken,
                          HttpServletRequest request, ModelAndView modelAndView, MessageSource messageSource) {
        super(request, modelAndView, messageSource);
        this.user = user;
        this.loginToken = loginToken;
    }

    public boolean validate() {
        String msg = "";

        this.message.put("status", "400");
        if (user == null) {
            this.message.put("message", "No login info.");
            return false;
        }

        if (MyStringUtils.isBlank(user.getUsername())) {
            this.message.put("message", getText("msg.username.or.password.is.blank"));
            return false;
        }

        if (MyStringUtils.isBlank(user.getPassword())) {
            msg = getText("msg.no.such.user.exception", new String[]{user.getPassword()});
            this.message.put("message", msg);
            return false;
        }

        if (MyStringUtils.isBlank(loginToken)) {
            this.message.put("message", getText("msg.no.login.token"));
            return false;
        }

        HttpSession session = request.getSession();
        MyToken myToken = (MyToken) session.getAttribute(KeyConstants.TOKEN.getKey());
        if (myToken == null) {
            this.message.put("message", getText("msg.no.login.token"));
            this.message.put("status", "400");
            return false;
        }

        if (!loginToken.equals(myToken.getLoginToken())) {
            this.message.put("message", getText("msg.login.token.not.match"));
            this.message.put("status", "400");
            return false;
        }

        this.message.put("status", "200");
        return true;
    }


}
