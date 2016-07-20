package com.forsrc.cxf.server.restful.login.service;


import com.forsrc.exception.NoSuchUserException;
import com.forsrc.exception.PasswordNotMatchException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

@Service
@Transactional
public interface LoginCxfService {

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public void login(SOAPMessage soapMessage) throws SOAPException, NoSuchUserException, PasswordNotMatchException;
}
