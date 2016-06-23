package com.forsrc.springmvc.demo.dao.impl;


import com.forsrc.springmvc.demo.dao.DemoDao;
import com.forsrc.springmvc.restful.base.dao.RestfulDao;
import com.forsrc.springmvc.restful.base.dao.impl.RestfulDaoImpl;

import java.io.Serializable;

//@Repository(value = "demoDao")
public class DemoDaoImpl<E, PK extends Serializable>
        extends RestfulDaoImpl<E, PK>
        implements DemoDao<E, PK>, RestfulDao<E, PK> {


}
