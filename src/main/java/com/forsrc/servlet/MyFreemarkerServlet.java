package com.forsrc.servlet;


import com.forsrc.utils.WebUtils;
import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.ext.servlet.FreemarkerServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type My freemarker servlet.
 */
public class MyFreemarkerServlet extends FreemarkerServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        //this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
        WebUtils.setContentType(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        WebUtils.setContentType(request, response);
    }
}
