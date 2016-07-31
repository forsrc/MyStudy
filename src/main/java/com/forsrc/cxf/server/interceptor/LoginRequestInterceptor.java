package com.forsrc.cxf.server.interceptor;


import com.forsrc.springmvc.restful.login.service.LoginService;
import com.forsrc.springmvc.restful.login.validator.LoginValidator;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The type Login request interceptor.
 */
public class LoginRequestInterceptor extends AbstractPhaseInterceptor<Message> {

    private SAAJInInterceptor saajInInterceptor;
    @Autowired
    @Resource(name = "loginService")
    private LoginService loginService;

    /**
     * The Message source.
     */
    @Autowired
    @Resource(name = "messageSource")
    protected MessageSource messageSource;

    /**
     * Instantiates a new Login request interceptor.
     */
    public LoginRequestInterceptor() {
        super(Phase.PRE_PROTOCOL);
        saajInInterceptor = new SAAJInInterceptor();
        super.getAfter().add(SAAJInInterceptor.class.getName());
    }

    @Override
    public void handleMessage(Message message) {

        HttpServletRequest request = (HttpServletRequest) message.get(
                AbstractHTTPDestination.HTTP_REQUEST);

        HttpSession session = request.getSession();


        LoginValidator loginValidator = new LoginValidator(request, messageSource);

        if (!loginValidator.validateAlreadyLogin()) {
            throw new IllegalArgumentException("No token or it does not match.");
        }

    }


}
