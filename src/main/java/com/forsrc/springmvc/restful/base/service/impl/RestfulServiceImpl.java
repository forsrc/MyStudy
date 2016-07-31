package com.forsrc.springmvc.restful.base.service.impl;

import com.forsrc.exception.ServiceException;
import com.forsrc.springmvc.restful.base.dao.RestfulDao;
import com.forsrc.springmvc.restful.base.service.RestfulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


/**
 * The type Restful service.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
//@Service(value = "restfulService")
//@Service
@Transactional
public abstract class RestfulServiceImpl<E, PK extends Serializable> implements RestfulService<E, PK> {

    /**
     * The Restful dao.
     */
//@Autowired
    //@Resource(name = "restfulDao")
    protected RestfulDao restfulDao;


    /**
     * Gets restful dao.
     *
     * @return the restful dao
     */
    public RestfulDao getRestfulDao() {
        return this.restfulDao;
    }

    /**
     * Sets restful dao.
     *
     * @param restfulDao the restful dao
     */
    public void setRestfulDao(RestfulDao restfulDao) {
        this.restfulDao = restfulDao;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<E> list(int start, int size) throws ServiceException {
        return this.getRestfulDao().list(start, size);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public E get(PK pk) throws ServiceException {
        return (E) this.getRestfulDao().get(pk);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void delete(List list) throws ServiceException {
        this.getRestfulDao().delete(list);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void delete(E e) throws ServiceException {
        this.getRestfulDao().delete(e);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public E update(E e) throws ServiceException {
        this.getRestfulDao().update(e);
        return e;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public E save(E e) throws ServiceException {
        return (E) this.getRestfulDao().save(e);
    }
}
