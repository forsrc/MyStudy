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
package com.forsrc.filter;

import com.forsrc.constant.KeyConstants;
import com.forsrc.utils.*;
import com.forsrc.utils.MyAesUtils.AesException;
import com.forsrc.utils.MyAesUtils.AesKey;
import com.forsrc.utils.MyDesUtils.DesException;
import com.forsrc.utils.MyDesUtils.DesKey;
import com.forsrc.utils.MyRsaUtils.RsaKey;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * The type Resource filter.
 */
public class ResourceFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(ResourceFilter.class);

    private static final String PATH_FILE_NAME = "/config/properties/resource.properties";

    private static Properties properties;

    /**
     * Sets resource.
     *
     * @param request  the request
     * @param response the response
     */
    public static void setResource(HttpServletRequest request, HttpServletResponse response) {

        //WebUtils.setContentType(request, response);
        if (request.getAttribute(KeyConstants.READY.getKey()) != null
                && (Boolean) request.getAttribute(KeyConstants.READY.getKey())) {
            return;
        }
        HttpSession session = request.getSession();
        Object hasError = session == null ? null : session.getAttribute("hasError");
        if (hasError != null && hasError instanceof Boolean && (Boolean) hasError) {
            String errorMessage = (String) request.getSession().getAttribute("errorMessage");
            Exception exception = (Exception) request.getSession().getAttribute("error");
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("error", exception);
            request.getSession().removeAttribute("errorMessage");
            request.getSession().removeAttribute("error");
            request.getSession().setAttribute("hasError", false);
        }

        String baseUrl = WebUtils.getBaseUrl(request);
        request.setAttribute(WebUtils.KEY_BASE_URL, baseUrl);
        request.setAttribute("basePath", request.getContextPath());
        request.setAttribute("request", request);
        RsaKey rsaKey = (RsaKey) request.getSession().getAttribute(
                KeyConstants.RSA_KEY.toString());
        AesKey aesKey = (AesKey) request.getSession().getAttribute(
                KeyConstants.AES_KEY.toString());
        DesKey desKey = (DesKey) request.getSession().getAttribute(
                KeyConstants.DES_KEY.toString());
        String restRsaKey = request.getParameter("restRsaKey");
        if (rsaKey == null || "true".equals(restRsaKey)) {
            rsaKey = new RsaKey();
            request.getSession().setAttribute(KeyConstants.RSA_KEY.toString(),
                    rsaKey);
        }
        String restAesKey = request.getParameter("restAesKey");
        if (aesKey == null || "true".equals(restAesKey)) {
            aesKey = new AesKey();
            request.getSession().setAttribute(KeyConstants.AES_KEY.toString(),
                    aesKey);
        }
        String restDesKey = request.getParameter("restDesKey");
        if (desKey == null || "true".equals(restDesKey)) {
            desKey = new DesKey();
            request.getSession().setAttribute(KeyConstants.DES_KEY.toString(),
                    desKey);
        }
        request.setAttribute("rsaN", rsaKey.getN().toString());
        request.setAttribute("rsaE", rsaKey.getE().toString());
        request.setAttribute("rsaD", rsaKey.getD().toString());
        request.setAttribute("aesKey", aesKey.getKey());
        request.setAttribute("aesKeyIv", aesKey.getIv());
        request.setAttribute("pwdRsa", MyRsaUtils.encrypt(rsaKey, "abc123$%您好")
                .toString());
        request.setAttribute("desKey", desKey.getKey());
        try {
            request.setAttribute("pwdAes", MyAesUtils.encrypt(aesKey, "abc123$%您好")
                    .toString());
        } catch (AesException e) {
            LOGGER.error(e.getMessage(), e);
        }
        try {
            request.setAttribute("pwdDes", MyDesUtils.encrypt(desKey, "abc123$%您好")
                    .toString());
        } catch (DesException e) {
            LOGGER.error(e.getMessage(), e);
        }
        WebUtils.setLanguage(request);
        getProperties();
        Set<Entry<Object, Object>> set = properties.entrySet();
        for (Entry<Object, Object> entry : set) {
            request.setAttribute(entry.getKey().toString(), entry.getValue()
                    .toString());
        }
        request.setAttribute(KeyConstants.READY.getKey(), true);
    }

    private static Properties getProperties() {
        if (properties != null) {
            return properties;
        }
        try {
            properties = PropertiesUtils.getProperties(PATH_FILE_NAME);
            return properties;
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        setResource(request, response);
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        getProperties();
    }

}
