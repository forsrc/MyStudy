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


/**
 * The interface Base hibernate search dao.
 *
 * @param <E> the type parameter
 */
//@Repository(value = "baseHibernateSearchDao")
public interface BaseHibernateSearchDao<E> extends BaseHibernateDao<E, Serializable> {

    /**
     * Query base hibernate search query entity.
     *
     * @param fields      the fields
     * @param keyword     the keyword
     * @param analyzer    the analyzer
     * @param parser      the parser
     * @param firstResult the first result
     * @param maxResults  the max results
     * @return the base hibernate search query entity
     * @throws DaoException the dao exception
     */
    public BaseHibernateSearchQueryEntity<E, E> query(String[] fields, String keyword,
                                                      Analyzer analyzer,
                                                      QueryParser parser,
                                                      int firstResult, int maxResults) throws DaoException;


    /**
     * Query base hibernate search query entity.
     *
     * @param fields      the fields
     * @param keyword     the keyword
     * @param analyzer    the analyzer
     * @param parser      the parser
     * @param formatter   the formatter
     * @param firstResult the first result
     * @param maxResults  the max results
     * @return the base hibernate search query entity
     * @throws DaoException the dao exception
     */
    public BaseHibernateSearchQueryEntity<E, BaseHibernateSearchQueryEntity.Entity<E>> query(String[] fields, String keyword,
                                                                                             Analyzer analyzer,
                                                                                             QueryParser parser,
                                                                                             Formatter formatter,
                                                                                             int firstResult, int maxResults) throws DaoException;

    /**
     * Index.
     *
     * @param cls the cls
     * @throws DaoException the dao exception
     */
    public void index(Class cls) throws DaoException;

    /**
     * Purge.
     *
     * @param fields      the fields
     * @param keyword     the keyword
     * @param parser      the parser
     * @param cls         the cls
     * @param firstResult the first result
     * @param maxResults  the max results
     * @throws DaoException the dao exception
     */
    public void purge(String[] fields, String keyword,
                      QueryParser parser, Class<E> cls,
                      int firstResult, int maxResults) throws DaoException;

    /**
     * Purge.
     *
     * @param cls the cls
     * @param pk  the pk
     * @throws DaoException the dao exception
     */
    public void purge(Class<E> cls, Serializable pk) throws DaoException;

    /**
     * Purge all.
     *
     * @param cls the cls
     */
    public void purgeAll(Class cls);

    /**
     * Config.
     *
     * @param fullTextSession the full text session
     * @param query           the query
     */
    public void config(final FullTextSession fullTextSession, final FullTextQuery query);

    /**
     * The type Base hibernate search query entity.
     *
     * @param <E> the type parameter
     * @param <L> the type parameter
     */
    @XmlRootElement(name = "BaseHibernateSearchQueryEntity")
    @org.codehaus.jackson.annotate.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
    public static class BaseHibernateSearchQueryEntity<E, L> {


        /**
         * The List.
         */
        List<L> list;

        private int firstResult;
        private int maxResults;
        private int total;

        /**
         * Instantiates a new Base hibernate search query entity.
         */
        public BaseHibernateSearchQueryEntity() {
            this.firstResult = 0;
            this.maxResults = 0;
            this.total = 0;
            this.list = new ArrayList<L>(this.maxResults);
        }

        /**
         * Instantiates a new Base hibernate search query entity.
         *
         * @param firstResult the first result
         * @param maxResults  the max results
         * @param total       the total
         * @param resultSize  the result size
         */
        public BaseHibernateSearchQueryEntity(int firstResult, int maxResults, int total, int resultSize) {
            this.firstResult = firstResult;
            this.maxResults = maxResults;
            this.total = total;
            this.list = new ArrayList<L>(resultSize);
        }

        /**
         * Gets first result.
         *
         * @return the first result
         */
        public int getFirstResult() {
            return this.firstResult;
        }

        /**
         * Sets first result.
         *
         * @param firstResult the first result
         */
        public void setFirstResult(int firstResult) {
            this.firstResult = firstResult;
        }

        /**
         * Gets max results.
         *
         * @return the max results
         */
        public int getMaxResults() {
            return this.maxResults;
        }

        /**
         * Sets max results.
         *
         * @param maxResults the max results
         */
        public void setMaxResults(int maxResults) {
            this.maxResults = maxResults;
        }

        /**
         * Gets total.
         *
         * @return the total
         */
        public int getTotal() {
            return this.total;
        }

        /**
         * Sets total.
         *
         * @param total the total
         */
        public void setTotal(int total) {
            this.total = total;
        }

        /**
         * Gets list.
         *
         * @return the list
         */
        public List<L> getList() {
            return this.list;
        }

        /**
         * Sets list.
         *
         * @param list the list
         */
        public void setList(List<L> list) {
            this.list = list;
        }

        /**
         * The type Entity.
         *
         * @param <E> the type parameter
         */
        @XmlRootElement(name = "BaseHibernateSearchQueryEntity.Entity")
        @org.codehaus.jackson.annotate.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
        @com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
        public static class Entity<E> {

            private E entity;
            private Map<String, String> highlighters;
            private Float score;

            /**
             * Instantiates a new Entity.
             */
            public Entity() {
            }

            /**
             * Instantiates a new Entity.
             *
             * @param entity       the entity
             * @param score        the score
             * @param highlighters the highlighters
             */
            public Entity(E entity, Float score, Map<String, String> highlighters) {
                this.entity = entity;
                this.score = score;
                this.highlighters = highlighters;
            }

            /**
             * Gets entity.
             *
             * @return the entity
             */
            public E getEntity() {
                return this.entity;
            }

            /**
             * Sets entity.
             *
             * @param entity the entity
             */
            public void setEntity(E entity) {
                this.entity = entity;
            }


            /**
             * Gets highlighters.
             *
             * @return the highlighters
             */
            public Map<String, String> getHighlighters() {
                return this.highlighters;
            }

            /**
             * Sets highlighters.
             *
             * @param highlighters the highlighters
             */
            public void setHighlighters(Map<String, String> highlighters) {
                this.highlighters = highlighters;
            }

            /**
             * Sets score.
             *
             * @param score the score
             */
            public void setScore(Float score) {
                this.score = score;
            }

            /**
             * Gets score.
             *
             * @return the score
             */
            public Float getScore() {
                return score;
            }
        }
    }
}
