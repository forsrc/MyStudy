package com.forsrc.base.dao;


/**
 * The interface Base dao entity class handler.
 *
 * @param <E> the type parameter
 */
public interface BaseDaoEntityClassHandler<E> {
    /**
     * Gets entity class.
     *
     * @return the class of E
     */
    public Class<E> getEntityClass();

    /**
     * set the class of E
     *
     * @param entityClass the class of E
     */
    public void setEntityClass(Class<E> entityClass);
}
