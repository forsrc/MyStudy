package com.forsrc.cxf.server.restful.login.service.impl;


import com.forsrc.cxf.server.restful.login.service.LoginCxfService;
import com.forsrc.exception.NoSuchUserException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.login.service.LoginService;
import org.apache.cxf.interceptor.Fault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

@Service(value = "loginCxfService")
@Transactional
public class LoginCxfServiceImpl implements LoginCxfService{

    @Autowired
    @Resource(name = "loginService")
    private LoginService loginService;


    @Override
    public void login(SOAPMessage soapMessage) throws SOAPException, NoSuchUserException, PasswordNotMatchException {

        SOAPHeader head = null;
        try {
            head = soapMessage.getSOAPHeader();
        } catch (SOAPException e) {
            throw e;
        }
        if (head == null) {
            SOAPException soapException = new SOAPException("Head is blank.");
            throw new Fault(soapException);
        }

        NodeList usernameNodeList = head.getElementsByTagName("tns:username");
        NodeList passwordNodeList = head.getElementsByTagName("tns:password");

        String username = usernameNodeList.item(0).getTextContent();
        String password = passwordNodeList.item(0).getTextContent();

        User user = new User();

        try {
            loginService.login(user);
        } catch (NoSuchUserException e) {
            throw e;
        } catch (PasswordNotMatchException e) {
            throw e;
        }

    }
}
