package com.forsrc.hibernate.search;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * The type Hibernate search application context aware.
 */
public class HibernateSearchApplicationContextAware implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    /**
     * Index.
     *
     * @param <E> the type parameter
     * @param e   the e
     */
    public <E> void index(E e) {
        HibernateSearchApplicationEvent event = new HibernateSearchApplicationEvent(
                HibernateSearchApplicationEvent.Op.OP_INDEX_CREATE, e);
        applicationContext.publishEvent(event);
    }

    /**
     * Index list.
     *
     * @param <E>  the type parameter
     * @param list the list
     */
    public <E> void indexList(List<E> list) {
        HibernateSearchApplicationEvent event = new HibernateSearchApplicationEvent(
                HibernateSearchApplicationEvent.Op.OP_INDEX_CREATE, list);
        applicationContext.publishEvent(event);
    }

    /**
     * Update.
     *
     * @param <E> the type parameter
     * @param e   the e
     */
    public <E> void update(E e) {

        HibernateSearchApplicationEvent event = new HibernateSearchApplicationEvent(
                HibernateSearchApplicationEvent.Op.OP_INDEX_UPDATE, e);
        applicationContext.publishEvent(event);
    }

    /**
     * Update list.
     *
     * @param <E>  the type parameter
     * @param list the list
     */
    public <E> void updateList(List<E> list) {
        HibernateSearchApplicationEvent event = new HibernateSearchApplicationEvent(
                HibernateSearchApplicationEvent.Op.OP_INDEX_UPDATE, list);
        applicationContext.publishEvent(event);
    }

    /**
     * Delete.
     *
     * @param <E> the type parameter
     * @param id  the id
     * @param e   the e
     */
    public <E> void delete(Long id, E e) {
        HibernateSearchApplicationEvent event = new HibernateSearchApplicationEvent(
                HibernateSearchApplicationEvent.Op.OP_INDEX_DELETE, e);
        applicationContext.publishEvent(event);
    }

    /**
     * Delete list.
     *
     * @param <E>  the type parameter
     * @param list the list
     */
    public <E> void deleteList(List<E> list) {
        HibernateSearchApplicationEvent event = new HibernateSearchApplicationEvent(
                HibernateSearchApplicationEvent.Op.OP_INDEX_DELETE, list);
        applicationContext.publishEvent(event);
    }


    /**
     * Gets application context.
     *
     * @return the application context
     */
    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
