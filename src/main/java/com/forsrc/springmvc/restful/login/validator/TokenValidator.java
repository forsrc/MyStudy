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

public class TokenValidator extends Validator {


    public TokenValidator(HttpServletRequest request, ModelAndView modelAndView, MessageSource messageSource) {
        super(request, modelAndView, messageSource);
    }

    public boolean validate(String token) {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(KeyConstants.USER.getKey());

        String msg = "";

        this.message.put("status", "400");
        if (user == null || user.getId() == null) {
            this.message.put("message", "No login info.");
            return false;
        }

        if (MyStringUtils.isBlank(token)) {
            this.message.put("message", getText("msg.no.login.token"));
            return false;
        }

        MyToken myToken = (MyToken) session.getAttribute(KeyConstants.TOKEN.getKey());
        if (myToken == null) {
            this.message.put("message", getText("msg.no.login.token"));
            this.message.put("status", "400");
            return false;
        }

        if (!token.equals(myToken.getLoginToken())) {
            this.message.put("message", getText("msg.login.token.not.match"));
            this.message.put("status", "400");
            return false;
        }

        this.message.put("status", "200");
        return true;
    }


}
