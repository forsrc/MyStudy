package com.forsrc.springmvc.demo.dao;

import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.springmvc.restful.base.dao.RestfulDao;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface DemoDao<E, PK extends Serializable>
        extends BaseHibernateDao<E, PK>, RestfulDao<E, PK> {

}
