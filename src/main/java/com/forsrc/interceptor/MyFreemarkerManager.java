package com.forsrc.interceptor;


import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;

public class MyFreemarkerManager extends
        org.apache.struts2.views.freemarker.FreemarkerManager {

    protected Configuration createConfiguration(ServletContext servletContext)
            throws TemplateException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring/spring_beans_freemarker.xml");
        FreeMarkerConfigurer configurer = (FreeMarkerConfigurer) ctx.getBean("freemarkerConfig");
        return configurer.getConfiguration();
    }
}
