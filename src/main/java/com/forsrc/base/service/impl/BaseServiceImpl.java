package com.forsrc.base.service.impl;

import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.base.service.BaseService;
import com.forsrc.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * The type Base service.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
//@Service()
//@Transactional
public abstract class BaseServiceImpl<E, PK extends Serializable> implements BaseService<E, PK> {


    /**
     * The Base hibernate dao.
     */
    protected BaseHibernateDao<E, PK> baseHibernateDao;

    @Override
    //@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<E> list(int start, int size) throws ServiceException {
        return getBaseHibernateDao().list(start, size);
    }

    @Override
    //@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public E get(PK pk) throws ServiceException {
        return (E) getBaseHibernateDao().get(pk);
    }

    @Override
    //@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public E load(PK pk) throws ServiceException {
        return (E) getBaseHibernateDao().load(pk);
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E save(E bean) throws ServiceException {
        return getBaseHibernateDao().save(bean);
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E update(E bean) throws ServiceException {
        return getBaseHibernateDao().update(bean);
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E merge(E bean) throws ServiceException {
        getBaseHibernateDao().merge(bean);
        return bean;
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void delete(E e) throws ServiceException {
        getBaseHibernateDao().delete(e);
    }

    @Override
    public void clean(){
        getBaseHibernateDao().clean();
    }

    @Override
    public void flush(){
        getBaseHibernateDao().clean();
    }

    public BaseHibernateDao<E, PK> getBaseHibernateDao() {
        return baseHibernateDao;
    }

    /**
     * Sets base hibernate dao.
     *
     * @param baseHibernateDao the base hibernate dao
     */
    public void setBaseHibernateDao(BaseHibernateDao<E, PK> baseHibernateDao) {
        this.baseHibernateDao = baseHibernateDao;
    }
}
