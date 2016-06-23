package com.forsrc.interceptor;

import com.forsrc.constant.KeyConstants;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {

        if (serverHttpRequest.getHeaders().containsKey("Sec-WebSocket-Extensions")) {
            serverHttpRequest.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");
        }
        HttpSession session = getSession(serverHttpRequest);
        if (session == null) {

            return super.beforeHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, attributes);
        }


        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
        HttpServletRequest request = servletRequest.getServletRequest();
        Map<String, String[]> parameterMap = request.getParameterMap();
        attributes.putAll(parameterMap);
        attributes.put("queryString", request.getQueryString());
        String username = (String) session.getAttribute(KeyConstants.USERNAME.getKey());
        if (username != null) {
            attributes.put(KeyConstants.USERNAME.getKey(), username);
        }

        return super.beforeHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }

    private HttpSession getSession(ServerHttpRequest request) {
        if(request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest)request;
            return serverRequest.getServletRequest().getSession(super.isCreateSession());
        }
        return null;
    }

}
