package com.forsrc.springmvc.demo.controller;

import com.forsrc.springmvc.demo.service.DemoService;
import com.forsrc.springmvc.restful.base.controller.RestfulController;
import com.forsrc.springmvc.restful.base.controller.impl.RestfulControllerImpl;

import java.io.Serializable;

/**
 * The type Demo controller.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
//@Controller
//@RequestMapping(value = "/")
public class DemoController<E, PK extends Serializable>
        extends RestfulControllerImpl<E, PK>
        implements RestfulController<E, PK> {

    //@Autowired
    //@Resource(name = "demoService")
    private DemoService demoService;


    /**
     * Gets demo service.
     *
     * @return the demo service
     */
    public DemoService getDemoService() {
        return this.demoService;
    }

    /**
     * Sets demo service.
     *
     * @param demoService the demo service
     */
    public void setDemoService(DemoService demoService) {
        this.demoService = demoService;
    }
}
