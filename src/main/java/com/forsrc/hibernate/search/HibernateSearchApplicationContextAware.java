package com.forsrc.hibernate.search;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

public class HibernateSearchApplicationContextAware implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    public <E> void index(E e) {
        HibernateSearchApplicationEvent event = new HibernateSearchApplicationEvent(
                HibernateSearchApplicationEvent.Op.OP_INDEX_CREATE, e);
        applicationContext.publishEvent(event);
    }

    public <E> void indexList(List<E> list) {
        HibernateSearchApplicationEvent event = new HibernateSearchApplicationEvent(
                HibernateSearchApplicationEvent.Op.OP_INDEX_CREATE, list);
        applicationContext.publishEvent(event);
    }

    public <E> void update(E e) {

        HibernateSearchApplicationEvent event = new HibernateSearchApplicationEvent(
                HibernateSearchApplicationEvent.Op.OP_INDEX_UPDATE, e);
        applicationContext.publishEvent(event);
    }

    public <E> void updateList(List<E> list) {
        HibernateSearchApplicationEvent event = new HibernateSearchApplicationEvent(
                HibernateSearchApplicationEvent.Op.OP_INDEX_UPDATE, list);
        applicationContext.publishEvent(event);
    }

    public <E> void delete(Long id, E e) {
        HibernateSearchApplicationEvent event = new HibernateSearchApplicationEvent(
                HibernateSearchApplicationEvent.Op.OP_INDEX_DELETE, e);
        applicationContext.publishEvent(event);
    }

    public <E> void deleteList(List<E> list) {
        HibernateSearchApplicationEvent event = new HibernateSearchApplicationEvent(
                HibernateSearchApplicationEvent.Op.OP_INDEX_DELETE, list);
        applicationContext.publishEvent(event);
    }


    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
