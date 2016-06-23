package com.forsrc.springmvc.restful.base.dao.impl;


import com.forsrc.exception.DaoException;
import com.forsrc.springmvc.base.dao.impl.BaseHibernateDaoImpl;
import com.forsrc.springmvc.restful.base.dao.RestfulDao;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

//@Repository
//@Repository(value = "restfulDao")
public abstract class RestfulDaoImpl<E, PK extends Serializable> extends BaseHibernateDaoImpl<E> implements RestfulDao<E, PK>{

    public int count() throws DaoException {
        return super.count();
    }
    public List<E> list(int start, int size) throws DaoException {
        return list(this.getEntityClass(), start, size);
    }

    @Override
    public E save(E e) throws DaoException {
        getHibernateTemplate().save(e);
        return e;
    }

    @Override
    public E update(E e) throws DaoException {
        getHibernateTemplate().update(e);
        return e;
    }

    @Override
    public void delete(E e) throws DaoException {
        getHibernateTemplate().delete(e);
    }

    @Override
    public E get(PK pk) throws DaoException {
        return getHibernateTemplate().get(getEntityClass(), pk);
    }

    @Override
    public E load(PK pk) throws DaoException {
        return getHibernateTemplate().load(getEntityClass(), pk);
    }

    @Override
    public void delete(List<E> list) {
        getHibernateTemplate().deleteAll(list);
    }

}
