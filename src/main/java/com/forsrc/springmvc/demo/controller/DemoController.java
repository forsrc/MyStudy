package com.forsrc.springmvc.demo.controller;

import com.forsrc.springmvc.demo.service.DemoService;
import com.forsrc.springmvc.restful.base.controller.RestfulController;
import com.forsrc.springmvc.restful.base.controller.impl.RestfulControllerImpl;

import java.io.Serializable;

//@Controller
//@RequestMapping(value = "/")
public class DemoController<E, PK extends Serializable>
        extends RestfulControllerImpl<E, PK>
        implements RestfulController<E, PK> {

    //@Autowired
    //@Resource(name = "demoService")
    private DemoService demoService;


    public DemoService getDemoService() {
        return this.demoService;
    }

    public void setDemoService(DemoService demoService) {
        this.demoService = demoService;
    }
}
