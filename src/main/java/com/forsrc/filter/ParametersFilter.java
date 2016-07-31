package com.forsrc.filter;


import com.forsrc.constant.KeyConstants;
import com.forsrc.utils.JacksonUtils;
import com.forsrc.utils.MyAesUtils;
import com.forsrc.utils.MyDesUtils;
import com.forsrc.utils.MyRsaUtils;
import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

/**
 * The type Parameters filter.
 */
public class ParametersFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(ParametersFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String characterEncoding = httpServletRequest.getParameter("setCharacterEncoding");
        if (characterEncoding != null) {
            httpServletRequest.setCharacterEncoding(characterEncoding);
            httpServletResponse.setCharacterEncoding(characterEncoding);
        }
        String contentType = httpServletRequest.getParameter("setContentType");
        if (contentType != null) {
            httpServletResponse.setContentType(contentType);
        }

        handleDes(httpServletRequest, "des", false);
        handleDes(httpServletRequest, "mydes", true);
        handleAes(httpServletRequest, "aes", false);
        handleAes(httpServletRequest, "myAes", true);
        handleRsa(httpServletRequest, "rsa", false);
        handleRsa(httpServletRequest, "myrsa", true);

        String id = httpServletRequest.getParameter("id");
        if (id != null && "_empty".equals(id)) {
            httpServletRequest.removeAttribute("id");
        }
        String header = httpServletRequest.getParameter("setHeader");
        if (header != null) {

            try {
                Map<String, String> map = JacksonUtils.asMap(String.class, String.class, header);
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    httpServletResponse.setHeader(entry.getKey(), entry.getValue());
                }
            } catch (IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        String sendRedirect = httpServletRequest.getParameter("sendRedirect");
        if (sendRedirect != null) {
            LOGGER.info(MessageFormat.format("{0} --> sendRedirect: {1}", httpServletRequest.getRequestURI(), sendRedirect));
            httpServletResponse.sendRedirect(sendRedirect);
        }
    }

    private void handleDes(HttpServletRequest httpServletRequest, String key, boolean isKeyEncrypted) {
        MyDesUtils.DesKey desKey = (MyDesUtils.DesKey) httpServletRequest.getSession().getAttribute(
                KeyConstants.DES_KEY.toString());
        String jsonStr = httpServletRequest.getParameter(key);
        if (jsonStr == null || desKey == null) {
            return;
        }
        try {
            Map<String, String> map = JacksonUtils.asMap(String.class, String.class, jsonStr);
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String k = entry.getKey();
                String v = entry.getValue();
                if (isKeyEncrypted) {
                    try {
                        k = MyDesUtils.decrypt(desKey, entry.getKey());
                    } catch (MyDesUtils.DesException e) {
                        //LOGGER.warn(e.getMessage(), e);
                        v = entry.getKey();
                    }
                }
                try {
                    v = MyDesUtils.decrypt(desKey, entry.getValue());
                } catch (MyDesUtils.DesException e) {
                    //LOGGER.warn(e.getMessage(), e);
                    v = entry.getValue();
                }
                httpServletRequest.setAttribute(k, v);
            }
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    private void handleAes(HttpServletRequest httpServletRequest, String key, boolean isKeyEncrypted) {
        MyAesUtils.AesKey aesKey = (MyAesUtils.AesKey) httpServletRequest.getSession().getAttribute(
                KeyConstants.AES_KEY.toString());
        String jsonStr = httpServletRequest.getParameter(key);
        if (jsonStr == null || aesKey == null) {
            return;
        }
        try {
            Map<String, String> map = JacksonUtils.asMap(String.class, String.class, jsonStr);
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String k = entry.getKey();
                String v = entry.getValue();

                if (isKeyEncrypted) {
                    try {
                        k = MyAesUtils.decrypt(aesKey, entry.getKey());
                    } catch (MyAesUtils.AesException e) {
                        //LOGGER.warn(e.getMessage(), e);
                        k = entry.getKey();
                    }
                }
                try {
                    v = MyAesUtils.decrypt(aesKey, entry.getValue());
                } catch (MyAesUtils.AesException e) {
                    //LOGGER.warn(e.getMessage(), e);
                    v = entry.getValue();
                }
                httpServletRequest.setAttribute(k, v);
            }
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    private void handleRsa(HttpServletRequest httpServletRequest, String key, boolean isKeyEncrypted) {
        MyRsaUtils.RsaKey rsaKey = (MyRsaUtils.RsaKey) httpServletRequest.getSession().getAttribute(
                KeyConstants.RSA_KEY.toString());

        String jsonStr = httpServletRequest.getParameter(key);
        if (jsonStr == null || rsaKey == null) {
            return;
        }
        try {
            Map<String, String> map = JacksonUtils.asMap(String.class, String.class, jsonStr);
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String k = entry.getKey();
                String v = entry.getValue();
                if (isKeyEncrypted) {
                    try {
                        k = MyRsaUtils.decrypt(rsaKey, entry.getKey());
                    } catch (IOException e) {
                        k = entry.getKey();
                    }
                }
                try {
                    v = MyRsaUtils.decrypt(rsaKey, entry.getValue());
                } catch (IOException e) {
                    v = entry.getValue();
                }
                httpServletRequest.setAttribute(k, v);
            }
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }
}
