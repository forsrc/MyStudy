package com.forsrc.springmvc.base.service;


import com.forsrc.exception.ServiceException;
import com.forsrc.springmvc.base.dao.BaseHibernateSearchDao;
import org.apache.lucene.search.highlight.Formatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
public interface BaseHibernateSearchService<E> {

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public BaseHibernateSearchDao.BaseHibernateSearchQueryEntity<E, E> query(String[] fields, String keyword,
                                                                             int firstResult, int maxResults) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public BaseHibernateSearchDao.BaseHibernateSearchQueryEntity<E,
            BaseHibernateSearchDao.BaseHibernateSearchQueryEntity.Entity<E>>
    query(String[] fields, String keyword,
          Formatter formatter,
          int firstResult, int maxResults) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void index(Class cls) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void purge(String[] fields, String keyword, Class cls,
                      int firstResult, int maxResults) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void purge(Class<E> cls, Serializable pk) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void purgeAll(Class cls);

}
