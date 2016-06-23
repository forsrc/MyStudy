package com.forsrc.springmvc.base.dao.impl;


import com.forsrc.exception.DaoException;
import com.forsrc.lucene.MySimpleFSDirectory;
import com.forsrc.springmvc.base.dao.BaseHibernateDao;
import com.forsrc.springmvc.base.dao.BaseHibernateSearchDao;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.hibernate.*;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository(value = "baseHibernateSearchDao")
public class BaseHibernateSearchDaoImpl<E> extends BaseHibernateDaoImpl<E> implements BaseHibernateSearchDao<E>, BaseHibernateDao<E> {

    public static final int DEF_MAX_SIZE = 10;
    public static final int BATCH_SIZE = 100;

    @Autowired
    @Qualifier("mySimpleFSDirectory")
    private MySimpleFSDirectory mySimpleFSDirectory;

    @Override
    public BaseHibernateSearchQueryEntity<E, E> query(String[] fields, String keyword, Analyzer analyzer, QueryParser parser, int firstResult, int maxResults) throws DaoException {

        Query luceneQuery = getQuery(parser, keyword);


        int max = maxResults <= 0 ? DEF_MAX_SIZE : maxResults;
        int first = firstResult <= 0 ? 0 : firstResult;
        Session session = super.getSessionFactory().openSession();

        FullTextSession fullTextSession = Search.getFullTextSession(session);
        Transaction transaction = fullTextSession.beginTransaction();
        // QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(getEntityClass()).get();
        //org.apache.lucene.search.Query luceneSearchQuery = queryBuilder.keyword().onFields(fields).matching(keyword).createQuery();

        FullTextQuery query = fullTextSession.createFullTextQuery(luceneQuery, this.getEntityClass());

        //org.hibernate.Query query = fullTextSession.createFullTextQuery(luceneSearchQuery, getClass());
        query.setCacheable(true);
        query.setMaxResults(max);
        query.setFirstResult(first);
        query.setTimeout(10, TimeUnit.SECONDS);
        config(fullTextSession, query);
        List<E> list = query.list();
        BaseHibernateSearchQueryEntity<E, E> entity =
                new BaseHibernateSearchQueryEntity<E, E>(firstResult, maxResults, query.getResultSize(), 0);
        entity.setList(list);
        try {
            transaction.commit();
        } catch (TransactionException e) {
            LOGGER.warn(e.getClass().getName() + ": " + e.getMessage());
        }
        fullTextSession.clear();
        fullTextSession.close();
        return entity;
    }

    @Override
    public void config(final FullTextSession fullTextSession, final FullTextQuery query) {

    }


    private Query getQuery(QueryParser parser, String keyword) throws DaoException {
        try {
            return parser.parse(keyword);
        } catch (ParseException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public BaseHibernateSearchQueryEntity<E, BaseHibernateSearchQueryEntity.Entity<E>> query(String[] fields, String keyword,
                                                                                             Analyzer analyzer,
                                                                                             QueryParser parser,
                                                                                             Formatter formatter,
                                                                                             int firstResult, int maxResults) throws DaoException {
        Query luceneQuery = getQuery(parser, keyword);
        int max = maxResults <= 0 ? DEF_MAX_SIZE : maxResults;
        int first = firstResult <= 0 ? 0 : firstResult;

        QueryScorer queryScorer = new QueryScorer(luceneQuery);
        Highlighter highlighter = new Highlighter(formatter, queryScorer);

        Session session = super.getSessionFactory().openSession();

        FullTextSession fullTextSession = Search.getFullTextSession(session);
        Transaction transaction = fullTextSession.beginTransaction();
        //org.hibernate.Query query = fullTextSession.createFullTextQuery(luceneQuery, this.getEntityClass());

        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery);
        fullTextQuery.setProjection(FullTextQuery.THIS, FullTextQuery.DOCUMENT, FullTextQuery.SCORE, FullTextQuery.ID);
        fullTextQuery.setMaxResults(max);
        fullTextQuery.setFirstResult(first);
        fullTextQuery.setCacheable(true);

        fullTextQuery.setTimeout(10, TimeUnit.SECONDS);

        config(fullTextSession, fullTextQuery);

        List<Object[]> queryResult = fullTextQuery.list();
        session.flush();
        if (queryResult == null || queryResult.isEmpty()) {
            return new BaseHibernateSearchQueryEntity<E, BaseHibernateSearchQueryEntity.Entity<E>>(firstResult, maxResults, 0, 0);
        }

        BaseHibernateSearchQueryEntity<E, BaseHibernateSearchQueryEntity.Entity<E>> result =
                new BaseHibernateSearchQueryEntity<E, BaseHibernateSearchQueryEntity.Entity<E>>(
                        firstResult, maxResults, fullTextQuery.getResultSize(), queryResult.size());

        boolean hasDirtyIndex = false;
        try {
            for (Object[] objects : queryResult) {
                E e = (E) (objects[0]);
                if (e == null) {
                    fullTextSession.purge(this.getEntityClass(), (Serializable) objects[3]);
                    hasDirtyIndex = true;
                    continue;
                }
                if (hasDirtyIndex) {
                    continue;
                }
                Document document = (Document) objects[1];
                Map<String, String> mapHighlighter = new HashMap<String, String>(fields.length);

                for (String field : fields) {
                    IndexableField indexableField = document.getField(field);
                    TokenStream tokenStream = analyzer.tokenStream(field, new StringReader(indexableField.stringValue()));
                    String content = highlighter.getBestFragment(tokenStream, indexableField.stringValue());
                    mapHighlighter.put(field, content);
                }

                BaseHibernateSearchQueryEntity.Entity<E> entity = new BaseHibernateSearchQueryEntity.Entity(
                        (E) (objects[0]), (Float) (objects[2]), mapHighlighter);

                result.getList().add(entity);
            }
            try {
                transaction.commit();
            } catch (TransactionException e) {
                LOGGER.warn(e.getClass().getName() + ": " + e.getMessage());
            }
            if (hasDirtyIndex) {
                fullTextSession.flushToIndexes();
                fullTextSession.clear();
                fullTextSession.close();
                fullTextSession = null;
                result.getList().clear();
                return query(fields, keyword, analyzer, parser, formatter, firstResult, maxResults);
            }
        } catch (IOException e) {
            transaction.rollback();
            throw new DaoException(e.getMessage());
        } catch (InvalidTokenOffsetsException e) {
            transaction.rollback();
            throw new DaoException(e.getMessage());
        } finally {
            if (fullTextSession != null) {
                fullTextSession.clear();
                fullTextSession.close();
            }
        }

        return result;
    }

    @Override
    public void purge(String[] fields, String keyword,
                      QueryParser parser, Class<E> cls,
                      int firstResult, int maxResults) throws DaoException {

        Query luceneQuery = getQuery(parser, keyword);

        //int max = maxResults <= 0 ? DEF_MAX_SIZE : maxResults;
        int first = firstResult <= 0 ? 0 : firstResult;
        Session session = super.getSessionFactory().openSession();

        FullTextSession fullTextSession = Search.getFullTextSession(session);
        Transaction transaction = fullTextSession.beginTransaction();
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, cls);

        fullTextQuery.setProjection(FullTextQuery.THIS, FullTextQuery.ID);
        if (maxResults > 0) {
            fullTextQuery.setMaxResults(maxResults);
        }
        fullTextQuery.setFirstResult(first);
        fullTextQuery.setTimeout(10, TimeUnit.SECONDS);
        config(fullTextSession, fullTextQuery);

        List<Object[]> list = fullTextQuery.list();
        for (Object[] objects : list) {
            E e = (E) (objects[0]);
            if (e == null) {
                fullTextSession.purge(cls, (Serializable) objects[1]);
                fullTextSession.flushToIndexes();
            }
        }
        try {
            transaction.commit();
        } catch (TransactionException e) {
            LOGGER.warn(e.getClass().getName() + ": " + e.getMessage());
        }
        fullTextSession.clear();
        fullTextSession.close();
    }

    @Override
    public void purge(Class<E> cls, Serializable pk) throws DaoException {

        FullTextSession fullTextSession = Search.getFullTextSession(
                super.getSessionFactory().openSession());
        Transaction transaction = fullTextSession.beginTransaction();
        fullTextSession.purge(cls, pk);
        fullTextSession.flushToIndexes();

        try {
            transaction.commit();
        } catch (TransactionException e) {
            LOGGER.warn(e.getClass().getName() + ": " + e.getMessage());
        }
        fullTextSession.clear();
        fullTextSession.close();
    }


    @Override
    public void purgeAll(Class cls) {
        Session session = super.getSessionFactory().openSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        Transaction transaction = fullTextSession.beginTransaction();
        fullTextSession.purgeAll(cls);
        try {
            transaction.commit();
        } catch (TransactionException e) {
            LOGGER.warn(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void index(Class cls) throws DaoException {
        Session session = super.getSessionFactory().openSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        fullTextSession.setFlushMode(FlushMode.MANUAL);
        fullTextSession.setCacheMode(CacheMode.IGNORE);
        Transaction transaction = fullTextSession.beginTransaction();

        ScrollableResults results = fullTextSession.createCriteria(cls)
                .setFetchSize(BATCH_SIZE)
                .scroll(ScrollMode.FORWARD_ONLY);
        int index = 0;
        while (results.next()) {
            index++;
            fullTextSession.index(results.get(0));
            if (index % BATCH_SIZE == 0) {
                fullTextSession.flushToIndexes();
                fullTextSession.clear();
            }
        }
        try {
            transaction.commit();
        } catch (TransactionException e) {
            LOGGER.warn(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public MySimpleFSDirectory getMySimpleFSDirectory() {
        return this.mySimpleFSDirectory;
    }

    public void setMySimpleFSDirectory(MySimpleFSDirectory mySimpleFSDirectory) {
        this.mySimpleFSDirectory = mySimpleFSDirectory;
    }
}
