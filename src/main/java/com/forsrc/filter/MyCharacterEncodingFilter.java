package com.forsrc.filter;


import com.forsrc.utils.WebUtils;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type My character encoding filter.
 */
public class MyCharacterEncodingFilter extends CharacterEncodingFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        WebUtils.setContentType(request, response);
        super.doFilterInternal(request, response, filterChain);
    }
}
