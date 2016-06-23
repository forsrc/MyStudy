package com.forsrc.utils;


import org.apache.cxf.transport.http.AbstractHTTPDestination;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;


public class CxfUtils {

    public static HttpServletRequest getHttpServletRequest(WebServiceContext webServiceContext) {
        MessageContext ctx = webServiceContext.getMessageContext();
        return (HttpServletRequest) ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
    }

    public static HttpServletResponse getHttpServletResponse(WebServiceContext webServiceContext) {
        MessageContext ctx = webServiceContext.getMessageContext();
        return (HttpServletResponse) ctx.get(AbstractHTTPDestination.HTTP_RESPONSE);
    }

}
