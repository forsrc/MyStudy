package com.forsrc.base.dao;

import com.forsrc.exception.DaoException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.highlight.Formatter;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//@Repository(value = "baseHibernateSearchDao")
public interface BaseHibernateSearchDao<E> extends BaseHibernateDao<E, Serializable> {

    public BaseHibernateSearchQueryEntity<E, E> query(String[] fields, String keyword,
                                                      Analyzer analyzer,
                                                      QueryParser parser,
                                                      int firstResult, int maxResults) throws DaoException;


    public BaseHibernateSearchQueryEntity<E, BaseHibernateSearchQueryEntity.Entity<E>> query(String[] fields, String keyword,
                                                                                             Analyzer analyzer,
                                                                                             QueryParser parser,
                                                                                             Formatter formatter,
                                                                                             int firstResult, int maxResults) throws DaoException;

    public void index(Class cls) throws DaoException;

    public void purge(String[] fields, String keyword,
                      QueryParser parser, Class<E> cls,
                      int firstResult, int maxResults) throws DaoException;

    public void purge(Class<E> cls, Serializable pk) throws DaoException;

    public void purgeAll(Class cls);

    public void config(final FullTextSession fullTextSession, final FullTextQuery query);

    @XmlRootElement(name = "BaseHibernateSearchQueryEntity")
    @org.codehaus.jackson.annotate.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
    public static class BaseHibernateSearchQueryEntity<E, L> {


        List<L> list;

        private int firstResult;
        private int maxResults;
        private int total;

        public BaseHibernateSearchQueryEntity() {
            this.firstResult = 0;
            this.maxResults = 0;
            this.total = 0;
            this.list = new ArrayList<L>(this.maxResults);
        }

        public BaseHibernateSearchQueryEntity(int firstResult, int maxResults, int total, int resultSize) {
            this.firstResult = firstResult;
            this.maxResults = maxResults;
            this.total = total;
            this.list = new ArrayList<L>(resultSize);
        }

        public int getFirstResult() {
            return this.firstResult;
        }

        public void setFirstResult(int firstResult) {
            this.firstResult = firstResult;
        }

        public int getMaxResults() {
            return this.maxResults;
        }

        public void setMaxResults(int maxResults) {
            this.maxResults = maxResults;
        }

        public int getTotal() {
            return this.total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<L> getList() {
            return this.list;
        }

        public void setList(List<L> list) {
            this.list = list;
        }

        @XmlRootElement(name = "BaseHibernateSearchQueryEntity.Entity")
        @org.codehaus.jackson.annotate.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
        @com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
        public static class Entity<E> {

            private E entity;
            private Map<String, String> highlighters;
            private Float score;

            public Entity() {
            }

            public Entity(E entity, Float score, Map<String, String> highlighters) {
                this.entity = entity;
                this.score = score;
                this.highlighters = highlighters;
            }

            public E getEntity() {
                return this.entity;
            }

            public void setEntity(E entity) {
                this.entity = entity;
            }


            public Map<String, String> getHighlighters() {
                return this.highlighters;
            }

            public void setHighlighters(Map<String, String> highlighters) {
                this.highlighters = highlighters;
            }

            public void setScore(Float score) {
                this.score = score;
            }

            public Float getScore() {
                return score;
            }
        }
    }
}
