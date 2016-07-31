package com.forsrc.utils;


import org.apache.cxf.transport.http.AbstractHTTPDestination;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;


/**
 * The type Cxf utils.
 */
public class CxfUtils {

    /**
     * Gets http servlet request.
     *
     * @param webServiceContext the web service context
     * @return the http servlet request
     */
    public static HttpServletRequest getHttpServletRequest(WebServiceContext webServiceContext) {
        MessageContext ctx = webServiceContext.getMessageContext();
        return (HttpServletRequest) ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
    }

    /**
     * Gets http servlet response.
     *
     * @param webServiceContext the web service context
     * @return the http servlet response
     */
    public static HttpServletResponse getHttpServletResponse(WebServiceContext webServiceContext) {
        MessageContext ctx = webServiceContext.getMessageContext();
        return (HttpServletResponse) ctx.get(AbstractHTTPDestination.HTTP_RESPONSE);
    }

}
