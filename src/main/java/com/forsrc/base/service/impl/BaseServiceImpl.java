package com.forsrc.base.service.impl;

import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.base.service.BaseService;
import com.forsrc.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

//@Service()
//@Transactional
public abstract class BaseServiceImpl<E, PK extends Serializable> implements BaseService<E, PK> {


    protected BaseHibernateDao<E, PK> dao;

    @Override
    //@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<E> list(int start, int size) throws ServiceException {
        return getDao().list(start, size);
    }

    @Override
    //@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public E get(PK pk) throws ServiceException {
        return (E) getDao().get(pk);
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E save(E bean) throws ServiceException {
        return getDao().save(bean);
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E update(E bean) throws ServiceException {
        return getDao().update(bean);
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E merge(E bean) throws ServiceException {
        getDao().merge(bean);
        return bean;
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void delete(E e) throws ServiceException {
        getDao().delete(e);
    }

    @Override
    public void clean(){
        getDao().clean();
    }

    @Override
    public void flush(){
        getDao().clean();
    }

    public BaseHibernateDao<E, PK> getDao() {
        return dao;
    }

    public void setDao(BaseHibernateDao<E, PK> dao) {
        this.dao = dao;
    }
}
