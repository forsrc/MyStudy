package com.forsrc.cxf.server.restful.base.dao;


import com.forsrc.exception.DaoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The interface Base cxf dao.
 */
@Repository()
public interface BaseCxfDao {

    /**
     * List list.
     *
     * @param <E>   the type parameter
     * @param cls   the cls
     * @param start the start
     * @param size  the size
     * @return the list
     * @throws DaoException the dao exception
     */
    public <E> List<E> list(Class<E> cls, int start, int size) throws DaoException;

    /**
     * Get e.
     *
     * @param <E>  the type parameter
     * @param <PK> the type parameter
     * @param cls  the cls
     * @param pk   the pk
     * @return the e
     * @throws DaoException the dao exception
     */
    public <E, PK extends Serializable> E get(Class<E> cls, PK pk) throws DaoException;

    /**
     * Count int.
     *
     * @param <E> the type parameter
     * @param cls the cls
     * @return the int
     * @throws DaoException the dao exception
     */
    public <E> int count(Class<E> cls) throws DaoException;

    /**
     * Load e.
     *
     * @param <E>  the type parameter
     * @param <PK> the type parameter
     * @param cls  the cls
     * @param pk   the pk
     * @return the e
     * @throws DaoException the dao exception
     */
    public <E, PK extends Serializable> E load(Class<E> cls, PK pk) throws DaoException;

    /**
     * Save e.
     *
     * @param <E> the type parameter
     * @param e   the e
     * @return the e
     * @throws DaoException the dao exception
     */
    public <E> E save(E e) throws DaoException;

    /**
     * Update e.
     *
     * @param <E> the type parameter
     * @param e   the e
     * @return the e
     * @throws DaoException                               the dao exception
     * @throws HibernateOptimisticLockingFailureException the hibernate optimistic locking failure exception
     * @throws DataIntegrityViolationException            the data integrity violation exception
     */
    public <E> E update(E e) throws DaoException, HibernateOptimisticLockingFailureException, DataIntegrityViolationException;

    /**
     * Merge e.
     *
     * @param <E> the type parameter
     * @param e   the e
     * @return the e
     * @throws DaoException                               the dao exception
     * @throws HibernateOptimisticLockingFailureException the hibernate optimistic locking failure exception
     * @throws DataIntegrityViolationException            the data integrity violation exception
     */
    public <E> E merge(E e) throws DaoException, HibernateOptimisticLockingFailureException, DataIntegrityViolationException;

    /**
     * Delete.
     *
     * @param <E> the type parameter
     * @param e   the e
     * @throws DaoException                    the dao exception
     * @throws DataIntegrityViolationException the data integrity violation exception
     */
    public <E> void delete(E e) throws DaoException, DataIntegrityViolationException;

    /**
     * Delete.
     *
     * @param <E>   the type parameter
     * @param <PK>  the type parameter
     * @param cls   the cls
     * @param id    the id
     * @param where the where
     * @throws DaoException                    the dao exception
     * @throws DataIntegrityViolationException the data integrity violation exception
     */
    public <E, PK extends Serializable> void delete(Class<E> cls, PK id, Map<String, Object> where) throws DaoException, DataIntegrityViolationException;

    /**
     * Clean.
     */
    public void clean();

    /**
     * Flush.
     */
    public void flush();
}
