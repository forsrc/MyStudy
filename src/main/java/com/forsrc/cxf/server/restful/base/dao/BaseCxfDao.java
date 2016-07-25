package com.forsrc.cxf.server.restful.base.dao;


import com.forsrc.exception.DaoException;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository()
public interface BaseCxfDao {

    public <E> List<E> list(Class<E> cls, int start, int size) throws DaoException;

    public <E, PK extends Serializable> E get(Class<E> cls, PK pk) throws DaoException;

    public <E, PK extends Serializable> E load(Class<E> cls, PK pk) throws DaoException;

    public <E> E save(E e) throws DaoException;

    public <E> E update(E e) throws DaoException, HibernateOptimisticLockingFailureException;

    public <E> E merge(E e) throws DaoException, HibernateOptimisticLockingFailureException;

    public <E> void delete(E e) throws DaoException;

    public void clean();

    public void flush();
}
