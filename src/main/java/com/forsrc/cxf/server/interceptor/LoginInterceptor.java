package com.forsrc.cxf.server.interceptor;


import com.forsrc.exception.NoSuchUserException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.login.service.LoginService;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The type Login interceptor.
 */
public class LoginInterceptor extends AbstractPhaseInterceptor<SoapMessage>
{

    private SAAJInInterceptor saajInInterceptor;
    @Autowired
    @Resource(name = "loginService")
    private LoginService loginService;

    /**
     * Instantiates a new Login interceptor.
     */
    public LoginInterceptor() {
        super(Phase.PRE_PROTOCOL);
        saajInInterceptor = new SAAJInInterceptor();
        super.getAfter().add(SAAJInInterceptor.class.getName());
    }

    @Override
    public void handleMessage(SoapMessage message) {

        System.out.println();
        Iterator<Map.Entry<String, Object>> it = message.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object>  entry = it.next();
            System.out.println(entry.getKey() + " --> " + (entry.getValue() == null ? "null" : entry.getValue().toString()));
        }

        System.out.println();
        System.out.println();

        List<Header> list = ((SoapMessage)message).getHeaders();
        for (Header header : list) {
            System.out.println(header.getName() + " -> " + header.toString());
        }

        SOAPMessage soapMessage = message.getContent(SOAPMessage.class);
        if (soapMessage == null) {
            saajInInterceptor.handleMessage((SoapMessage)message);
            soapMessage = message.getContent(SOAPMessage.class);
        }
        SOAPHeader head = null;
        try {
            head = soapMessage.getSOAPHeader();
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        if (head == null) {
            return;
        }

        NodeList usernameNodeList = head.getElementsByTagName("tns:username");
        NodeList passwordNodeList = head.getElementsByTagName("tns:password");

        String username = usernameNodeList.item(0).getTextContent();
        String password = passwordNodeList.item(0).getTextContent();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        try {
            loginService.login(user);
        } catch (NoSuchUserException e) {
            SOAPException soapExc = new SOAPException(e.getMessage());
            throw new Fault(soapExc);
        } catch (PasswordNotMatchException e) {
            SOAPException soapExc = new SOAPException(e.getMessage());
            throw new Fault(soapExc);
        } catch (ServiceException e) {
            SOAPException soapExc = new SOAPException(e.getMessage());
            throw new Fault(soapExc);
        }


    }


}
