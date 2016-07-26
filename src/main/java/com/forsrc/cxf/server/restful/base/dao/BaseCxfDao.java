package com.forsrc.cxf.server.restful.base.dao;


import com.forsrc.exception.DaoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Repository()
public interface BaseCxfDao {

    public <E> List<E> list(Class<E> cls, int start, int size) throws DaoException;

    public <E, PK extends Serializable> E get(Class<E> cls, PK pk) throws DaoException;

    public <E> int count(Class<E> cls) throws DaoException;

    public <E, PK extends Serializable> E load(Class<E> cls, PK pk) throws DaoException;

    public <E> E save(E e) throws DaoException;

    public <E> E update(E e) throws DaoException, HibernateOptimisticLockingFailureException, DataIntegrityViolationException;

    public <E> E merge(E e) throws DaoException, HibernateOptimisticLockingFailureException, DataIntegrityViolationException;

    public <E> void delete(E e) throws DaoException, DataIntegrityViolationException;

    public <E, PK extends Serializable> void delete(Class<E> cls, PK id, Map<String, Object> where) throws DaoException, DataIntegrityViolationException;

    public void clean();

    public void flush();
}
