package com.forsrc.springmvc.demo.service.impl;

import com.forsrc.springmvc.demo.dao.DemoDao;
import com.forsrc.springmvc.demo.service.DemoService;
import com.forsrc.springmvc.restful.base.service.RestfulService;
import com.forsrc.springmvc.restful.base.service.impl.RestfulServiceImpl;

import java.io.Serializable;

//@Service(value = "demoService")
//@Transactional
public class DemoServiceImpl<E, PK extends Serializable>
        extends RestfulServiceImpl<E, PK>
        implements DemoService<E, PK>, RestfulService<E, PK> {

    //@Autowired
    //@Resource(name = "demoDao")
    private DemoDao demoDao;

    public DemoDao getDemoDao() {
        return this.demoDao;
    }

    public void setDemoDao(DemoDao demoDao) {
        this.demoDao = demoDao;
    }
}
