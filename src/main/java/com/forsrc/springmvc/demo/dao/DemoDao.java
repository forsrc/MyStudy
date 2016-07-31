package com.forsrc.springmvc.demo.dao;

import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.springmvc.restful.base.dao.RestfulDao;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * The interface Demo dao.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
@Repository
public interface DemoDao<E, PK extends Serializable>
        extends BaseHibernateDao<E, PK>, RestfulDao<E, PK> {

}
