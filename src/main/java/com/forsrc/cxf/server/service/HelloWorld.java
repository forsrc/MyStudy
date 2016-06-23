package com.forsrc.cxf.server.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface HelloWorld {
    @WebMethod
    public String echo(@WebParam(name = "text") String text);
}
