package com.forsrc.base.service.impl;

import com.forsrc.exception.DaoException;
import com.forsrc.exception.ServiceException;
import com.forsrc.lucene.MySimpleFSDirectory;
import com.forsrc.base.dao.BaseHibernateSearchDao;
import com.forsrc.base.service.BaseHibernateSearchService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;

/**
 * The type Base hibernate search service.
 *
 * @param <E> the type parameter
 */
@Service(value = "baseHibernateSearchServiceImpl")
@Transactional
public abstract class BaseHibernateSearchServiceImpl<E> implements BaseHibernateSearchService<E> {

    /**
     * The Analyzer.
     */
    protected Analyzer analyzer;

    /**
     * The Base hibernate search dao.
     */
    @Autowired
    @Resource(name = "baseHibernateSearchDao")
    protected BaseHibernateSearchDao<E> baseHibernateSearchDao;
    @Autowired
    @Qualifier("mySimpleFSDirectory")
    private MySimpleFSDirectory mySimpleFSDirectory;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public BaseHibernateSearchDao.BaseHibernateSearchQueryEntity<E, E>
    query(String[] fields, String keyword,
          int firstResult, int maxResults) throws ServiceException {

        QueryParser parser = new MultiFieldQueryParser(fields, this.getAnalyzer());
        parser.setDefaultOperator(QueryParser.Operator.OR);
        parser.setAllowLeadingWildcard(true);
        return this.getBaseHibernateSearchDao().query(fields, keyword, this.getAnalyzer(), parser, firstResult, maxResults);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public BaseHibernateSearchDao.BaseHibernateSearchQueryEntity<E,
            BaseHibernateSearchDao.BaseHibernateSearchQueryEntity.Entity<E>>
    query(String[] fields, String keyword, Formatter formatter, int firstResult, int maxResults) throws ServiceException {


        QueryParser parser = new MultiFieldQueryParser(fields, this.getAnalyzer());

        parser.setDefaultOperator(QueryParser.Operator.OR);
        parser.setAllowLeadingWildcard(true);

        formatter = formatter == null ? new SimpleHTMLFormatter("<span style='color: red'>", "</span>") : formatter;


        return this.getBaseHibernateSearchDao().query(fields, keyword, this.getAnalyzer(), parser, formatter, firstResult, maxResults);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void index(Class cls) throws ServiceException {
        this.getBaseHibernateSearchDao().index(cls);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void purge(String[] fields, String keyword, Class cls,
                      int firstResult, int maxResults) throws ServiceException {
        QueryParser parser = new MultiFieldQueryParser(fields, this.getAnalyzer());
        this.getBaseHibernateSearchDao().purge(fields, keyword, parser, cls, firstResult, maxResults);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void purgeAll(Class cls) {

        this.getBaseHibernateSearchDao().purgeAll(cls);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void purge(Class<E> cls, Serializable pk) throws ServiceException {
        this.getBaseHibernateSearchDao().purge(cls, pk);
    }

    private QueryParser getQueryParser(String[] fields) {
        QueryParser parser = new MultiFieldQueryParser(fields, this.getAnalyzer());
        parser.setDefaultOperator(QueryParser.Operator.OR);
        parser.setAllowLeadingWildcard(true);
        return parser;
    }


    private IndexWriter getIndexWriter(MySimpleFSDirectory mySimpleFSDirectory, Analyzer analyzer) throws DaoException {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = null;
        try {
            indexWriter = new IndexWriter(mySimpleFSDirectory, indexWriterConfig);
        } catch (IOException e) {
            throw new DaoException(e.getMessage());
        }
        return indexWriter;
    }


    /**
     * Gets base hibernate search dao.
     *
     * @return the base hibernate search dao
     */
    public BaseHibernateSearchDao getBaseHibernateSearchDao() {
        return this.baseHibernateSearchDao;
    }

    /**
     * Sets base hibernate search dao.
     *
     * @param baseHibernateSearchDao the base hibernate search dao
     */
    public void setBaseHibernateSearchDao(BaseHibernateSearchDao baseHibernateSearchDao) {
        this.baseHibernateSearchDao = baseHibernateSearchDao;
    }

    /**
     * Gets analyzer.
     *
     * @return the analyzer
     */
    public Analyzer getAnalyzer() {
        return this.analyzer == null ? new SmartChineseAnalyzer() : this.analyzer;
    }

    /**
     * Sets analyzer.
     *
     * @param analyzer the analyzer
     */
    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * Gets my simple fs directory.
     *
     * @return the my simple fs directory
     */
    public MySimpleFSDirectory getMySimpleFSDirectory() {
        return this.mySimpleFSDirectory;
    }

    /**
     * Sets my simple fs directory.
     *
     * @param mySimpleFSDirectory the my simple fs directory
     */
    public void setMySimpleFSDirectory(MySimpleFSDirectory mySimpleFSDirectory) {
        this.mySimpleFSDirectory = mySimpleFSDirectory;
    }
}
