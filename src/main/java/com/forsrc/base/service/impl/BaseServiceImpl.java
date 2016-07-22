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
        return (E) getDao().load(pk);
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E save(E bean) throws ServiceException {
        getDao().save(bean);
        return bean;
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E update(E bean) throws ServiceException {
        getDao().update(bean);
        return bean;
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E merge(E bean) throws ServiceException {
        getDao().merge(bean);
        return bean;
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void delete(PK pk) throws ServiceException {
        getDao().delete(pk);
    }

    public void clean() throws ServiceException {
        getDao().clean();
    }

    public BaseHibernateDao getDao() {
        return dao;
    }

    public void setDao(BaseHibernateDao dao) {
        this.dao = dao;
    }
}
