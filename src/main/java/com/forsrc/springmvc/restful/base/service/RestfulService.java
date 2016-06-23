package com.forsrc.springmvc.restful.base.service;


import com.forsrc.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public interface RestfulService<E, PK extends Serializable> {

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<E> list(int start, int size) throws ServiceException;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public E get(PK pk) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public E save(E e) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public E update(E e) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void delete(E e) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void delete(List<E> list) throws ServiceException;

}
