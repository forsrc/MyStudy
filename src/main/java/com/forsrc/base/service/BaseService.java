package com.forsrc.base.service;


import com.forsrc.exception.DaoException;
import com.forsrc.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public interface BaseService<E, PK extends Serializable> {
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<E> list(int start, int size) throws ServiceException;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public E get(PK pk) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E save(E user) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E update(E user) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E merge(E user) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void delete(PK pk) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void clean() throws ServiceException;


}
