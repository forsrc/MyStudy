package com.forsrc.cxf.server.provider;


import com.forsrc.cxf.server.restful.login.service.LoginCxfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;

import javax.annotation.Resource;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.Provider;
import java.io.IOException;


/**
 * The type Login provider.
 */
public class LoginProvider implements Provider<DOMSource> {

    @Autowired
    @Resource(name = "loginCxfService")
    private LoginCxfService loginCxfService;

    @Override
    public DOMSource invoke(DOMSource request) {

        MessageFactory factory = null;
        try {
            factory = MessageFactory.newInstance();
            SOAPMessage soapMessage = factory.createMessage();
            soapMessage.getSOAPPart().setContent(request);

            System.out.println("SoapProvider -->");
            soapMessage.writeTo(System.out);
            System.out.println();

            loginCxfService.login(soapMessage);

            SOAPBody soapBody = soapMessage.getSOAPBody();

            Node node = soapBody.getFirstChild();


            DOMSource response = new DOMSource(soapMessage.getSOAPPart());
            return request;

        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}

