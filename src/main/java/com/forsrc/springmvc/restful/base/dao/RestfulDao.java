package com.forsrc.springmvc.restful.base.dao;

import com.forsrc.base.dao.BaseHibernateDao;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface RestfulDao<E, PK extends Serializable> extends BaseHibernateDao<E, PK> {

}
