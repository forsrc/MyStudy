package com.forsrc.springmvc.restful.base.service;


import com.forsrc.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * The interface Restful service.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
@Service
@Transactional
public interface RestfulService<E, PK extends Serializable> {

    /**
     * List list.
     *
     * @param start the start
     * @param size  the size
     * @return the list
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<E> list(int start, int size) throws ServiceException;

    /**
     * Get e.
     *
     * @param pk the pk
     * @return the e
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public E get(PK pk) throws ServiceException;

    /**
     * Save e.
     *
     * @param e the e
     * @return the e
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public E save(E e) throws ServiceException;

    /**
     * Update e.
     *
     * @param e the e
     * @return the e
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public E update(E e) throws ServiceException;

    /**
     * Delete.
     *
     * @param e the e
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void delete(E e) throws ServiceException;

    /**
     * Delete.
     *
     * @param list the list
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void delete(List<E> list) throws ServiceException;

}
