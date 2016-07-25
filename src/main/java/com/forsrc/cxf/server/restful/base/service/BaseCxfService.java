package com.forsrc.cxf.server.restful.base.service;


import com.forsrc.exception.DaoException;
import com.forsrc.exception.ServiceException;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public interface BaseCxfService <E, PK>{

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public <E> List<E> list(Class<E> cls, Integer start, Integer size) throws ServiceException;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public <E, PK extends Serializable> E get(Class<E> cls, PK pk) throws ServiceException;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public <E> int count(Class<E> cls) throws ServiceException;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public <E, PK extends Serializable> E load(Class<E> cls, PK pk) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public <E> E save(E user) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public <E> E update(E e) throws ServiceException, HibernateOptimisticLockingFailureException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public <E> E merge(E e) throws ServiceException, HibernateOptimisticLockingFailureException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public <E> void delete(E e) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void clean();

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void flush();

}
