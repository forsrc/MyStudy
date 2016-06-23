package com.forsrc.springmvc.base.dao;


public interface BaseDaoEntityClassHandler<E> {
    /**
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
