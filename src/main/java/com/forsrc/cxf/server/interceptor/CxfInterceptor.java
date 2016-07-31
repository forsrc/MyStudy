package com.forsrc.cxf.server.interceptor;


import com.forsrc.springmvc.restful.login.service.LoginService;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;

/**
 * The type Cxf interceptor.
 */
public class CxfInterceptor extends AbstractPhaseInterceptor<Message> {

    private SAAJInInterceptor saajInInterceptor;
    @Autowired
    @Resource(name = "loginService")
    private LoginService loginService;

    /**
     * Instantiates a new Cxf interceptor.
     */
    public CxfInterceptor() {
        super(Phase.PRE_PROTOCOL);
        saajInInterceptor = new SAAJInInterceptor();
        super.getAfter().add(SAAJInInterceptor.class.getName());
    }

    @Override
    public void handleMessage(Message message) {

        System.out.println();
        Iterator<Map.Entry<String, Object>> it = message.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            System.out.println(entry.getKey() + " --> " + (entry.getValue() == null ? "null" : entry.getValue().toString()));
        }

        System.out.println();
        System.out.println();

    }


}
