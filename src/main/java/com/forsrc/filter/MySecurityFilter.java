package com.forsrc.filter;

import com.forsrc.constant.KeyConstants;
import com.forsrc.pojo.User;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.*;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

/**
 * The type My security filter.
 */
public class MySecurityFilter extends AbstractSecurityInterceptor implements
        Filter {

    private FilterInvocationSecurityMetadataSource securityMetadataSource;


    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);

        Collection<ConfigAttribute> attributes = securityMetadataSource.getAttributes(fi);

        HttpSession session = fi.getHttpRequest().getSession();
        User user = (User) session.getAttribute(KeyConstants.USER.getKey());


        InterceptorStatusToken token = super.beforeInvocation(fi);
        if(user == null){

            //super.afterInvocation(token, null);
            //return;
        }


        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * Gets security metadata source.
     *
     * @return the security metadata source
     */
    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return securityMetadataSource;
    }

    /**
     * Sets security metadata source.
     *
     * @param securityMetadataSource the security metadata source
     */
    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

}
