package com.forsrc.base.service;


import com.forsrc.exception.ServiceException;
import com.forsrc.base.dao.BaseHibernateSearchDao;
import org.apache.lucene.search.highlight.Formatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * The interface Base hibernate search service.
 *
 * @param <E> the type parameter
 */
@Service
@Transactional
public interface BaseHibernateSearchService<E> {

    /**
     * Query base hibernate search dao . base hibernate search query entity.
     *
     * @param fields      the fields
     * @param keyword     the keyword
     * @param firstResult the first result
     * @param maxResults  the max results
     * @return the base hibernate search dao . base hibernate search query entity
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public BaseHibernateSearchDao.BaseHibernateSearchQueryEntity<E, E> query(String[] fields, String keyword,
                                                                             int firstResult, int maxResults) throws ServiceException;

    /**
     * Query base hibernate search dao . base hibernate search query entity < e , base hibernate search dao . base hibernate search query entity . entity.
     *
     * @param fields      the fields
     * @param keyword     the keyword
     * @param formatter   the formatter
     * @param firstResult the first result
     * @param maxResults  the max results
     * @return the base hibernate search dao . base hibernate search query entity < e , base hibernate search dao . base hibernate search query entity . entity
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public BaseHibernateSearchDao.BaseHibernateSearchQueryEntity<E,
            BaseHibernateSearchDao.BaseHibernateSearchQueryEntity.Entity<E>>
    query(String[] fields, String keyword,
          Formatter formatter,
          int firstResult, int maxResults) throws ServiceException;

    /**
     * Index.
     *
     * @param cls the cls
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void index(Class cls) throws ServiceException;

    /**
     * Purge.
     *
     * @param fields      the fields
     * @param keyword     the keyword
     * @param cls         the cls
     * @param firstResult the first result
     * @param maxResults  the max results
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void purge(String[] fields, String keyword, Class cls,
                      int firstResult, int maxResults) throws ServiceException;

    /**
     * Purge.
     *
     * @param cls the cls
     * @param pk  the pk
     * @throws ServiceException the service exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void purge(Class<E> cls, Serializable pk) throws ServiceException;

    /**
     * Purge all.
     *
     * @param cls the cls
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void purgeAll(Class cls);

}
