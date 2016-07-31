package com.forsrc.cxf.server.restful.base.service;


import com.forsrc.exception.DaoException;
import com.forsrc.exception.ServiceException;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The interface Base cxf service.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
@Service
@Transactional
public interface BaseCxfService <E, PK>{

    /**
     * List list.
     *
     * @param <E>   the type parameter
     * @param cls   the cls
     * @param start the start
     * @param size  the size
     * @return the list
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public <E> List<E> list(Class<E> cls, Integer start, Integer size) throws ServiceException;

    /**
     * Get e.
     *
     * @param <E>  the type parameter
     * @param <PK> the type parameter
     * @param cls  the cls
     * @param pk   the pk
     * @return the e
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public <E, PK extends Serializable> E get(Class<E> cls, PK pk) throws ServiceException;

    /**
     * Count int.
     *
     * @param <E> the type parameter
     * @param cls the cls
     * @return the int
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public <E> int count(Class<E> cls) throws ServiceException;

    /**
     * Load e.
     *
     * @param <E>  the type parameter
     * @param <PK> the type parameter
     * @param cls  the cls
     * @param pk   the pk
     * @return the e
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public <E, PK extends Serializable> E load(Class<E> cls, PK pk) throws ServiceException;

    /**
     * Save e.
     *
     * @param <E>  the type parameter
     * @param user the user
     * @return the e
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public <E> E save(E user) throws ServiceException;

    /**
     * Update e.
     *
     * @param <E> the type parameter
     * @param e   the e
     * @return the e
     * @throws ServiceException                           the service exception
     * @throws HibernateOptimisticLockingFailureException the hibernate optimistic locking failure exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public <E> E update(E e) throws ServiceException, HibernateOptimisticLockingFailureException;

    /**
     * Merge e.
     *
     * @param <E> the type parameter
     * @param e   the e
     * @return the e
     * @throws ServiceException                           the service exception
     * @throws HibernateOptimisticLockingFailureException the hibernate optimistic locking failure exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public <E> E merge(E e) throws ServiceException, HibernateOptimisticLockingFailureException;

    /**
     * Delete.
     *
     * @param <E> the type parameter
     * @param e   the e
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public <E> void delete(E e) throws ServiceException;

    /**
     * Delete.
     *
     * @param <E>   the type parameter
     * @param <PK>  the type parameter
     * @param cls   the cls
     * @param id    the id
     * @param where the where
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public <E, PK extends Serializable> void delete(Class<E> cls, PK id, final Map<String, Object> where) throws ServiceException;


    /**
     * Clean.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void clean();

    /**
     * Flush.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void flush();

}
