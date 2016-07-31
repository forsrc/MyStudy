package com.forsrc.cxf.server.demo.impl;


import com.forsrc.cxf.server.demo.HelloWorld;
import com.forsrc.utils.CxfUtils;
import org.springframework.stereotype.Component;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 * The type Hello world.
 */
@Component
@WebService(targetNamespace = "http://service.server.cxf.forsrc.com/", endpointInterface = "com.forsrc.cxf.server.demo.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

    //@Resource(name ="org.apache.cxf.jaxws.context.WebServiceContextImpl")
    private WebServiceContext context = new org.apache.cxf.jaxws.context.WebServiceContextImpl();

    @Override
    public String echo(@WebParam(name = "text") String text) {
        String id = CxfUtils.getHttpServletRequest(context).getParameter("id");
        CxfUtils.getHttpServletResponse(context).setHeader("id", id);
        System.out.println("id: " + id);
        return "Hello world : " + text;
    }


    /**
     * Gets context.
     *
     * @return the context
     */
    public WebServiceContext getContext() {
        return this.context;
    }

    /**
     * Sets context.
     *
     * @param context the context
     */
    public void setContext(WebServiceContext context) {
        this.context = context;
    }
}
