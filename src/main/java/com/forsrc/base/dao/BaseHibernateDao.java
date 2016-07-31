package com.forsrc.base.dao;


import com.forsrc.exception.DaoException;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The interface Base hibernate dao.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
@Repository
public interface BaseHibernateDao<E, PK extends Serializable> extends BaseDaoEntityClassHandler<E> {

    /**
     * The constant LOGGER.
     */
    public static final Logger LOGGER = Logger.getLogger(BaseHibernateDao.class.getName());

    /**
     * Gets hibernate template.
     *
     * @return the hibernate template
     */
    public HibernateTemplate getHibernateTemplate();

    /**
     * Gets session factory.
     *
     * @return the session factory
     */
    public SessionFactory getSessionFactory();

    /**
     * Count int.
     *
     * @return the int
     * @throws DaoException the dao exception
     */
    public int count() throws DaoException;

    /**
     * List list.
     *
     * @param begin the begin
     * @param size  the size
     * @return the list
     * @throws DaoException the dao exception
     */
    public List<E> list(int begin, int size) throws DaoException;

    /**
     * Get e.
     *
     * @param pk the pk
     * @return the e
     * @throws DaoException the dao exception
     */
    public E get(PK pk) throws DaoException;

    /**
     * Load e.
     *
     * @param pk the pk
     * @return the e
     * @throws DaoException the dao exception
     */
    public E load(PK pk) throws DaoException;

    /**
     * Save e.
     *
     * @param e the e
     * @return the e
     * @throws DaoException the dao exception
     */
    public E save(E e) throws DaoException;

    /**
     * Update e.
     *
     * @param e the e
     * @return the e
     * @throws DaoException                               the dao exception
     * @throws HibernateOptimisticLockingFailureException the hibernate optimistic locking failure exception
     */
    public E update(E e) throws DaoException, HibernateOptimisticLockingFailureException;

    /**
     * Merge e.
     *
     * @param e the e
     * @return the e
     * @throws DaoException                               the dao exception
     * @throws HibernateOptimisticLockingFailureException the hibernate optimistic locking failure exception
     */
    public E merge(E e) throws DaoException, HibernateOptimisticLockingFailureException;

    /**
     * Delete.
     *
     * @param e the e
     * @throws DaoException the dao exception
     */
    public void delete(E e) throws DaoException;

    /**
     * Delete.
     *
     * @param id    the id
     * @param where the where
     * @throws DaoException the dao exception
     */
    public void delete(PK id, final Map<String, Object> where) throws DaoException;

    /**
     * Delete.
     *
     * @param list the list
     * @throws DaoException the dao exception
     */
    public void delete(List<E> list) throws DaoException;

    /**
     * Flush.
     */
    public void flush();

    /**
     * Clean.
     */
    public void clean();
}
