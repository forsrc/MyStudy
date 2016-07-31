package com.forsrc.cxf.server.restful.login.service;


import com.forsrc.exception.NoSuchUserException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/**
 * The interface Login cxf service.
 */
@Service
@Transactional
public interface LoginCxfService {

    /**
     * Login.
     *
     * @param soapMessage the soap message
     * @throws SOAPException    the soap exception
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public void login(SOAPMessage soapMessage) throws SOAPException, ServiceException;
}
