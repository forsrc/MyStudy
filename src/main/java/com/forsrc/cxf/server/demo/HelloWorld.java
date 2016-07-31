package com.forsrc.cxf.server.demo;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * The interface Hello world.
 */
@WebService
public interface HelloWorld {
    /**
     * Echo string.
     *
     * @param text the text
     * @return the string
     */
    @WebMethod
    public String echo(@WebParam(name = "text") String text);
}
