package com.forsrc.base.service;


import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.exception.DaoException;
import com.forsrc.exception.ServiceException;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * The interface Base service.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
@Service
@Transactional
public interface BaseService<E, PK extends Serializable> {
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
     * Load e.
     *
     * @param pk the pk
     * @return the e
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public E load(PK pk) throws ServiceException;

    /**
     * Save e.
     *
     * @param user the user
     * @return the e
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E save(E user) throws ServiceException;

    /**
     * Update e.
     *
     * @param user the user
     * @return the e
     * @throws ServiceException                           the service exception
     * @throws HibernateOptimisticLockingFailureException the hibernate optimistic locking failure exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E update(E user) throws ServiceException, HibernateOptimisticLockingFailureException;

    /**
     * Merge e.
     *
     * @param user the user
     * @return the e
     * @throws ServiceException                           the service exception
     * @throws HibernateOptimisticLockingFailureException the hibernate optimistic locking failure exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public E merge(E user) throws ServiceException, HibernateOptimisticLockingFailureException;

    /**
     * Delete.
     *
     * @param e the e
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void delete(E e) throws ServiceException;

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


    /**
     * Gets base hibernate dao.
     *
     * @return the base hibernate dao
     */
    public BaseHibernateDao<E, PK> getBaseHibernateDao();

    //public void setBaseHibernateDao(BaseHibernateDao<E, PK> baseHibernateDao);

}
