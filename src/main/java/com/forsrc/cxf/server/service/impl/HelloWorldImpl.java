package com.forsrc.cxf.server.service.impl;


import com.forsrc.cxf.server.service.HelloWorld;
import com.forsrc.utils.CxfUtils;
import org.springframework.stereotype.Component;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

@Component
@WebService(targetNamespace = "http://service.server.cxf.forsrc.com/", endpointInterface = "com.forsrc.cxf.server.service.HelloWorld")
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


    public WebServiceContext getContext() {
        return this.context;
    }

    public void setContext(WebServiceContext context) {
        this.context = context;
    }
}
